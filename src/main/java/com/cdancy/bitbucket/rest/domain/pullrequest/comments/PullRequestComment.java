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

package com.cdancy.bitbucket.rest.domain.pullrequest.comments;

import java.util.List;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.cdancy.bitbucket.rest.domain.Properties;
import com.cdancy.bitbucket.rest.domain.User;
import com.cdancy.bitbucket.rest.error.Error;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PullRequestComment {
    public abstract Properties properties();

    public abstract Integer id();

    public abstract Integer version();

    public abstract String text();

    public abstract User author();

    public abstract long createdDate();

    public abstract long updatedDate();

    public abstract List<PullRequestComment> comments();

    public abstract Anchor anchor();

    public abstract PermittedOperations permittedOperations();

    @Nullable
    public abstract List<Error> errors();

    @SerializedNames({ "properties", "id", "version", "text", "author",
            "createdDate", "updatedDate", "comments", "anchor",
            "permittedOperations", "errors" })
    public static PullRequestComment create(Properties properties, Integer id,
            Integer version, String text, User author, long createdDate,
            long updatedDate, List<PullRequestComment> comments, Anchor anchor,
            PermittedOperations permittedOperations, List<Error> errors) {
        return new AutoValue_PullRequestComment(properties, id, version, text,
                author, createdDate, updatedDate, comments, anchor,
                permittedOperations, errors);
    }

}
