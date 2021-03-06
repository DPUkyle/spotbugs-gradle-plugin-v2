/*
 * Copyright 2019 SpotBugs team
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.spotbugs.snom.internal;

import com.android.build.gradle.tasks.AndroidJavaCompile;
import com.github.spotbugs.snom.SpotBugsTask;
import java.util.Objects;
import javax.inject.Inject;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.workers.WorkerExecutor;

@CacheableTask
class SpotBugsTaskForAndroid extends SpotBugsTask {
  private AndroidJavaCompile task;

  @Inject
  public SpotBugsTaskForAndroid(ObjectFactory objects, WorkerExecutor workerExecutor) {
    super(objects, workerExecutor);
  }

  void setTask(AndroidJavaCompile task) {
    this.task = Objects.requireNonNull(task);
    dependsOn(task);
  }

  @Override
  public FileCollection getSourceDirs() {
    return task.getSource();
  }

  @Override
  public FileCollection getClassDirs() {
    return task.getOutputDirectory().getAsFileTree();
  }

  @Override
  public FileCollection getAuxClassPaths() {
    return task.getClasspath();
  }
}
