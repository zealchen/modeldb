import * as React from 'react';
import { connect, ReactReduxContextValue } from 'react-redux';
import { RouteComponentProps } from 'react-router';

import { DeployButton, DeployManager } from 'components/Deploy';
import loader from 'components/images/loader.gif';
import tagStyles from 'components/TagBlock/TagBlock.module.css';
import { IArtifact } from 'models/Artifact';
import { IHyperparameter } from 'models/HyperParameters';
import { IMetric } from 'models/Metrics';
import ModelRecord from 'models/ModelRecord';
import routes, { GetRouteParams } from 'routes';
import {
  checkDeployStatusUntilDeployed,
  fetchDataStatisticsActionTypes,
  getDataStatistics,
  getServiceStatistics,
  selectDataStatistics,
  selectDeployStatusInfo,
  selectIsLoadingDataStatistics,
  selectIsLoadingServiceStatistics,
  selectServiceStatistics,
} from 'store/deploy';
import { fetchModelRecord } from 'store/model-record';
import { IApplicationState, IConnectedReduxProps } from 'store/store';

import {
  IDataStatistics,
  IDeployStatusInfo,
  IServiceStatistics,
  IUnknownStatusInfo,
} from 'models/Deploy';
import styles from './ModelRecord.module.css';
import ShowContentBasedOnUrl from './ShowContentBasedOnUrl/ShowContentBasedOnUrl';

type IUrlProps = GetRouteParams<typeof routes.modelRecord>;

interface IPropsFromState {
  data: ModelRecord | null;
  loadingModelRecord: boolean;
  deployState: IDeployStatusInfo | null;
  serviceStatistics: IServiceStatistics | null;
  dataStatistics: IDataStatistics | null;
  loadingServiceStatistics: boolean;
  loadingDataStatistics: boolean;
}

type AllProps = RouteComponentProps<IUrlProps> &
  IPropsFromState &
  IConnectedReduxProps;

class ModelRecordLayout extends React.PureComponent<AllProps> {
  public componentDidMount() {
    this.props.dispatch(
      fetchModelRecord(this.props.match.params.modelRecordId)
    );
    if (this.props.data) {
      this.props.dispatch(checkDeployStatusUntilDeployed(this.props.data.id));
      this.props.dispatch(getServiceStatistics(this.props.data.id));
      this.props.dispatch(getDataStatistics(this.props.data.id));
    }
  }

  public componentDidUpdate(prevProps: AllProps) {
    if (this.props.data && prevProps.data !== this.props.data) {
      this.props.dispatch(checkDeployStatusUntilDeployed(this.props.data.id));
    }

    if (
      this.props.data &&
      prevProps.deployState &&
      prevProps.deployState.status !== 'deployed' &&
      this.props.deployState &&
      this.props.deployState.status === 'deployed'
    ) {
      this.props.dispatch(getServiceStatistics(this.props.data.id));
      this.props.dispatch(getDataStatistics(this.props.data.id));
    }
  }

  public render() {
    const { data, loadingModelRecord: loading, deployState } = this.props;
    const alreadyDeployed = deployState && deployState.status === 'deployed';

    return loading ? (
      <img src={loader} className={styles.loader} />
    ) : data ? (
      <div className={styles.model_layout}>
        <div className={styles.record_summary}>
          <div className={styles.record_summary_main}>
            <div className={styles.record_label}>Name</div>
            <div className={styles.record_name}>{data.name}</div>
            <br />
            <div className={styles.record_label}>Tags</div>
            <div>
              {data.tags &&
                data.tags.map((value: string, key: number) => {
                  return (
                    <div className={styles.tags} key={key}>
                      <span className={tagStyles.staticTag}>{value}</span>
                    </div>
                  );
                })}
            </div>
          </div>
          <div className={styles.record_summary_meta}>
            {this.renderParmaLink('Model ID:', `${data.id.slice(0, 4)}..`)}
            {this.renderParmaLink(
              'Project ID:',
              `${data.projectId.slice(0, 4)}..`
            )}
            {this.renderParmaLink(
              'Experiment ID:',
              `${data.experimentId.slice(0, 4)}..`
            )}
          </div>
        </div>
        <this.Record header="Code version">{data.codeVersion}</this.Record>
        {data.hyperparameters && (
          <this.Record header="Hyperparameters">
            {data.hyperparameters.map((value: IHyperparameter, key: number) => {
              return (
                <div key={key}>
                  {value.key}: {value.value}
                </div>
              );
            })}
          </this.Record>
        )}
        {data.metrics && (
          <this.Record header="Metrics">
            {data.metrics.map((value: IMetric, key: number) => {
              return (
                <div key={key}>
                  {value.key}: {value.value}
                </div>
              );
            })}
          </this.Record>
        )}
        {data.artifacts && (
          <this.Record header="Artifacts">
            {data.artifacts.map((value: IArtifact, key: number) => {
              return (
                <div key={key}>
                  {value.key}: <ShowContentBasedOnUrl path={value.path} />
                </div>
              );
            })}
          </this.Record>
        )}
        <this.Record header="Deploy info">
          <>
            <DeployButton modelId={data.id} />
            <DeployManager />
          </>
        </this.Record>
        <this.Record
          header="Monitoring information"
          additionalHeaderClassName={styles.record_header_divider}
          additionalContainerClassName={styles.record_divider}
        />
        {(!deployState || deployState.status !== 'deployed') && (
          <this.Record header="No monitoring information" />
        )}
        {alreadyDeployed && (
          <this.Record header="Latency and service metrics">
            {this.props.loadingServiceStatistics ? (
              <img src={loader} className={styles.loader} />
            ) : this.props.serviceStatistics ? (
              JSON.stringify(this.props.serviceStatistics)
            ) : (
              ''
            )}
          </this.Record>
        )}
        {alreadyDeployed && (
          <this.Record header="Data metrics">
            {this.props.loadingDataStatistics ? (
              <img src={loader} className={styles.loader} />
            ) : this.props.dataStatistics ? (
              JSON.stringify(this.props.dataStatistics)
            ) : (
              ''
            )}
          </this.Record>
        )}
      </div>
    ) : (
      ''
    );
  }

  // tslint:disable-next-line: function-name
  private Record(props: {
    header: string;
    children?: React.ReactNode;
    additionalValueClassName?: string;
    additionalContainerClassName?: string;
    additionalHeaderClassName?: string;
  }) {
    const {
      header,
      children,
      additionalValueClassName,
      additionalContainerClassName,
      additionalHeaderClassName,
    } = props;
    return (
      <div className={`${styles.record} ${additionalContainerClassName}`}>
        <div className={`${styles.record_header} ${additionalHeaderClassName}`}>
          {header}
        </div>
        <div className={`${styles.record_value} ${additionalValueClassName}`}>
          {children}
        </div>
      </div>
    );
  }

  private renderParmaLink(label: string, value: string) {
    return (
      <div className={styles.experiment_link}>
        <span className={styles.parma_link_label}>{label}</span>{' '}
        <span className={styles.parma_link_value}>{value}</span>
      </div>
    );
  }
}

const mapStateToProps = (state: IApplicationState): IPropsFromState => {
  const { modelRecord } = state;
  return {
    data: modelRecord.data,
    dataStatistics: selectDataStatistics(state),
    deployState: modelRecord.data
      ? selectDeployStatusInfo(state, modelRecord.data.id)
      : null,
    loadingDataStatistics: selectIsLoadingDataStatistics(state),
    loadingModelRecord: modelRecord.loading,
    loadingServiceStatistics: selectIsLoadingServiceStatistics(state),
    serviceStatistics: selectServiceStatistics(state),
  };
};

export default connect(mapStateToProps)(ModelRecordLayout);
