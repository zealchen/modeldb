package ai.verta.modeldb.blobs;

import static ai.verta.modeldb.blobs.Utils.enforceOneof;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model.*;
import ai.verta.modeldb.versioning.blob.diff.*;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.util.HashSet;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class DiffAndMerge {
  @Property
  public void diffAndMerge(AutogenBlob a, AutogenBlob b) throws ModelDBException {
    AutogenBlob newA = enforceOneof(a);
    AutogenBlob newB = enforceOneof(b);
    AutogenBlobDiff d = DiffComputer.computeBlobDiff(newA, newB);

    // Applying the diff on top of the original A should get original B
    AutogenBlob diffedB = DiffMerger.mergeBlob(newA, d, new HashSet<String>());
    assertEquals(newB, diffedB);

    // Reapplying the diff should not change the result
    HashSet<String> conflictSet = new HashSet<String>();
    diffedB = DiffMerger.mergeBlob(diffedB, d, conflictSet);
    if (conflictSet.isEmpty()) {
      assertEquals(newB, diffedB);
    }
  }

//  @Property
//  public void diffAndMergeAutogenCode(AutogenCodeBlob a, AutogenCodeBlob b)
//      throws ModelDBException {
//    AutogenCodeBlob newA = enforceOneof(a);
//    AutogenCodeBlob newB = enforceOneof(b);
//    AutogenCodeDiff d = DiffComputer.computeCodeDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenCodeBlob diffedB = DiffMerger.mergeCode(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeCode(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenConfig(AutogenConfigBlob a, AutogenConfigBlob b)
//      throws ModelDBException {
//    AutogenConfigBlob newA = enforceOneof(a);
//    AutogenConfigBlob newB = enforceOneof(b);
//    AutogenConfigDiff d = DiffComputer.computeConfigDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenConfigBlob diffedB = DiffMerger.mergeConfig(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeConfig(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenDataset(AutogenDatasetBlob a, AutogenDatasetBlob b)
//      throws ModelDBException {
//    AutogenDatasetBlob newA = enforceOneof(a);
//    AutogenDatasetBlob newB = enforceOneof(b);
//    AutogenDatasetDiff d = DiffComputer.computeDatasetDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenDatasetBlob diffedB = DiffMerger.mergeDataset(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeDataset(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenDockerEnvironment(
//      AutogenDockerEnvironmentBlob a, AutogenDockerEnvironmentBlob b) throws ModelDBException {
//    AutogenDockerEnvironmentBlob newA = enforceOneof(a);
//    AutogenDockerEnvironmentBlob newB = enforceOneof(b);
//    AutogenDockerEnvironmentDiff d = DiffComputer.computeDockerEnvironmentDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenDockerEnvironmentBlob diffedB =
//        DiffMerger.mergeDockerEnvironment(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeDockerEnvironment(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenEnvironment(AutogenEnvironmentBlob a, AutogenEnvironmentBlob b)
//      throws ModelDBException {
//    AutogenEnvironmentBlob newA = enforceOneof(a);
//    AutogenEnvironmentBlob newB = enforceOneof(b);
//    AutogenEnvironmentDiff d = DiffComputer.computeEnvironmentDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenEnvironmentBlob diffedB = DiffMerger.mergeEnvironment(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeEnvironment(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenGitCode(AutogenGitCodeBlob a, AutogenGitCodeBlob b)
//      throws ModelDBException {
//    AutogenGitCodeBlob newA = enforceOneof(a);
//    AutogenGitCodeBlob newB = enforceOneof(b);
//    AutogenGitCodeDiff d = DiffComputer.computeGitCodeDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenGitCodeBlob diffedB = DiffMerger.mergeGitCode(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeGitCode(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenNotebookCode(AutogenNotebookCodeBlob a, AutogenNotebookCodeBlob b)
//      throws ModelDBException {
//    AutogenNotebookCodeBlob newA = enforceOneof(a);
//    AutogenNotebookCodeBlob newB = enforceOneof(b);
//    AutogenNotebookCodeDiff d = DiffComputer.computeNotebookCodeDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenNotebookCodeBlob diffedB = DiffMerger.mergeNotebookCode(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeNotebookCode(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenPathDataset(AutogenPathDatasetBlob a, AutogenPathDatasetBlob b)
//      throws ModelDBException {
//    AutogenPathDatasetBlob newA = enforceOneof(a);
//    AutogenPathDatasetBlob newB = enforceOneof(b);
//    AutogenPathDatasetDiff d = DiffComputer.computePathDatasetDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenPathDatasetBlob diffedB = DiffMerger.mergePathDataset(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergePathDataset(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenPythonEnvironment(
//      AutogenPythonEnvironmentBlob a, AutogenPythonEnvironmentBlob b) throws ModelDBException {
//    AutogenPythonEnvironmentBlob newA = enforceOneof(a);
//    AutogenPythonEnvironmentBlob newB = enforceOneof(b);
//    AutogenPythonEnvironmentDiff d = DiffComputer.computePythonEnvironmentDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenPythonEnvironmentBlob diffedB =
//        DiffMerger.mergePythonEnvironment(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergePythonEnvironment(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenS3Dataset(AutogenS3DatasetBlob a, AutogenS3DatasetBlob b)
//      throws ModelDBException {
//    AutogenS3DatasetBlob newA = enforceOneof(a);
//    AutogenS3DatasetBlob newB = enforceOneof(b);
//    AutogenS3DatasetDiff d = DiffComputer.computeS3DatasetDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenS3DatasetBlob diffedB = DiffMerger.mergeS3Dataset(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeS3Dataset(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
//
//  @Property
//  public void diffAndMergeAutogenVersionEnvironment(
//      AutogenVersionEnvironmentBlob a, AutogenVersionEnvironmentBlob b) throws ModelDBException {
//    AutogenVersionEnvironmentBlob newA = enforceOneof(a);
//    AutogenVersionEnvironmentBlob newB = enforceOneof(b);
//    AutogenVersionEnvironmentDiff d = DiffComputer.computeVersionEnvironmentDiff(newA, newB);
//
//    // Applying the diff on top of the original A should get original B
//    AutogenVersionEnvironmentBlob diffedB =
//        DiffMerger.mergeVersionEnvironment(newA, d, new HashSet<String>());
//    assertEquals(newB, diffedB);
//
//    HashSet<String> conflictSet = new HashSet<String>();
//    // Reapplying the diff should not change the result
//    diffedB = DiffMerger.mergeVersionEnvironment(diffedB, d, conflictSet);
//    if (conflictSet.isEmpty()) {
//      assertEquals(newB, diffedB);
//    }
//  }
}
