/**
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
package com.github.spotbugs.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.tasks.JavaExec;
import org.immutables.value.Value;

@Value.Immutable
abstract class SpotBugsSpec {
  /**
   * @return The {@code maxHeapSize} for the JVM process. Configured by {@link SpotBugsExtension}.
   */
  abstract Optional<String> maxHeapSize();

  /** @return The SpotBugs .jar file and its dependencies. Configured by Gradle Configuration. */
  abstract List<File> spotbugsJar();

  /** @return The plugin files. Configured by Gradle Configuration. */
  abstract List<File> plugins();

  /**
   * @return The flag to ignore exit code of SpotBugs execution. Configured by {@link
   *     SpotBugsExtension}.
   */
  abstract boolean isIgnoreFailures();

  /** @return The name of the generated task. */
  abstract String name();

  abstract List<File> sourceDirs();

  abstract List<File> classDirs();

  abstract List<File> auxClassPaths();

  abstract List<String> extraArguments();

  abstract List<String> jvmArgs();

  void applyTo(JavaExec javaExec) {
    javaExec.classpath(spotbugsJar());
    javaExec.setArgs(generateArguments());
    javaExec.setJvmArgs(jvmArgs());
    maxHeapSize().ifPresent(javaExec::setMaxHeapSize);
  }

  private List<String> generateArguments() {
    List<String> args = new ArrayList<>();
    if (!plugins().isEmpty()) {
      args.add("-pluginList");
      args.add(
          plugins().stream()
              .map(File::getAbsolutePath)
              .collect(Collectors.joining(File.pathSeparator)));
    }

    args.add("-sortByClass");
    args.add("-timestampNow");
    // TODO classDirs
    // TODO auxClassPaths
    // TODO sourceDirs
    args.addAll(extraArguments());
    return args;
  }
}