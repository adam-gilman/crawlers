/* Copyright 2023 Norconex Inc.
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
package com.norconex.importer.spi;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;

import com.norconex.commons.lang.ClassFinder;
import com.norconex.commons.lang.bean.BeanMapper;
import com.norconex.commons.lang.bean.spi.PolymorphicTypeProvider;
import com.norconex.importer.handler.DocumentHandler;

/**
 * <p>
 * For auto registering in {@link BeanMapper}.
 * </p>
 */
public class ImporterPtProvider implements PolymorphicTypeProvider {

    @Override
    public MultiValuedMap<Class<?>, Class<?>> getPolymorphicTypes() {
        MultiValuedMap<Class<?>, Class<?>> map =
                MultiMapUtils.newListValuedHashMap();
        map.putAll(DocumentHandler.class,
                ClassFinder.findSubTypes(DocumentHandler.class,
                        nm -> nm.startsWith("com.norconex.importer.handler")));
//        map.put(Consumer.class, Consumers.class);
        return map;
    }
}
