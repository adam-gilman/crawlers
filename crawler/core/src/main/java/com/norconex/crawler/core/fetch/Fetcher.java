/* Copyright 2022-2022 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.crawler.core.fetch;

/**
 * Fetch resources.
 * @param <T> fetch request type
 * @param <R> fetch response type
 */
//public interface Fetcher {
//
//    boolean accept(FetchRequest fetchRequest);
//
//    FetchResponse fetch(FetchRequest fetchRequest) throws FetchException;
//}
public interface Fetcher<T extends FetchRequest, R extends FetchResponse> {

    boolean accept(T fetchRequest);

    R fetch(T fetchRequest) throws FetchException;
}
