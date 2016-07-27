/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cdancy.bitbucket.rest.options;

import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Anchor {
    Anchor() {
    }

    public abstract int line();

    public abstract LineType lineType();

    public abstract FileType fileType();

    public abstract String path();

    @SerializedNames({ "line", "lineType", "fileType", "path" })
    public static AutoValue_Anchor create(int line, LineType lineType,
            FileType fileType, String path) {
        return new AutoValue_Anchor(line, lineType, fileType, path);
    }
}
