import * as React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router';
import { IArtifact } from '../../models/Artifact';
import { IHyperparameter } from '../../models/HyperParameters';
import { IMetric } from '../../models/Metrics';
import ModelRecord from '../../models/ModelRecord';
import { fetchModelRecord } from '../../store/model-record';
import { IApplicationState, IConnectedReduxProps } from '../../store/store';
import loader from '../images/loader.gif';
import ShowContentBasedOnUrl from '../ShowContentBasedOnUrl/ShowContentBasedOnUrl';
import styles from './ModelRecord.module.css';

export interface IUrlProps {
  modelId: string;
  projectId: string;
}

interface IPropsFromState {
  data?: ModelRecord | null;
  loading: boolean;
}

type AllProps = RouteComponentProps<IUrlProps> & IPropsFromState & IConnectedReduxProps;

class ModelRecordLayout extends React.Component<AllProps> {
  public render() {
    const { data, loading } = this.props;
    const notNullModel = data || new ModelRecord();

    return loading ? (
      <img src={loader} className={styles.loader} />
    ) : data ? (
      <div className={styles.model_layout}>
        {this.renderTextRecord('Name', notNullModel.Name, styles.name)}
        {this.renderTextRecord('Model Id', notNullModel.Id)}
        {this.renderTextRecord('Project Id', notNullModel.ProjectId)}
        {this.renderTextRecord('Experiment Id', notNullModel.ExperimentId)}
        {this.renderTextRecord('Code version', notNullModel.CodeVersion)}
        {this.renderRecord(
          'Tags',
          data.Tags.map((value: string, key: number) => {
            return (
              <div className={styles.tag} key={key}>
                <span className={styles.tag_text}>{value}</span>
              </div>
            );
          })
        )}
        {this.renderListRecord(
          'Hyperparameters',
          data.Hyperparameters.map((value: IHyperparameter, key: number) => {
            return (
              <div key={key}>
                {value.key}: {value.value}
              </div>
            );
          })
        )}
        {this.renderListRecord(
          'Metrics',
          notNullModel.Metric.map((value: IMetric, key: number) => {
            return (
              <div key={key}>
                {value.key}: {value.value}
              </div>
            );
          })
        )}
        {this.renderListRecord(
          'Artifacts',
          data.Artifacts.map((value: IArtifact, key: number) => {
            return (
              <div key={key}>
                {value.key}: <ShowContentBasedOnUrl path={value.path} />
              </div>
            );
          })
        )}
      </div>
    ) : (
      ''
    );
  }

  public componentDidMount() {
    this.props.dispatch(fetchModelRecord(this.props.match.params.modelId));
  }

  private renderRecord(header: string, content: JSX.Element[], additionalValueClassName: string = '') {
    return content && content.length > 0 ? (
      <div className={styles.record}>
        <div className={styles.record_header}>{header}</div>
        <div className={`${styles.record_value} ${additionalValueClassName}`}>{content}</div>
      </div>
    ) : (
      ''
    );
  }

  private renderTextRecord(header: string, value: string, additionalValueClassName: string = '') {
    return value ? this.renderRecord(header, [<span key={0}>{value}</span>], additionalValueClassName) : '';
  }

  private renderListRecord(header: string, content: JSX.Element[]) {
    return this.renderRecord(header, content, styles.list);
  }
}

const mapStateToProps = ({ modelRecord }: IApplicationState) => ({
  data: modelRecord.data,
  loading: modelRecord.loading
});

export default connect(mapStateToProps)(ModelRecordLayout);