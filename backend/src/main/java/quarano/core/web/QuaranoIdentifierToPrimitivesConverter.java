/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.core.web;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jddd.core.types.Identifier;
import org.modelmapper.Converter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/**
 * A Spring {@link Converter} to convert from {@link Identifier} types to both {@link UUID} and {@link String} assuming
 * the {@link Identifier} implementation contains a single property of type {@link UUID}. If the target is
 * {@link String} we simply also call {@link Object#toString()} on the {@link UUID}.
 *
 * @author Oliver Drotbohm
 */
@Component
public class QuaranoIdentifierToPrimitivesConverter implements GenericConverter {

	private static final Map<Class<?>, Field> CACHE = new ConcurrentReferenceHashMap<>();

	public static Stream<Class<?>> getIdPrimitives() {
		return Stream.of(UUID.class, String.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
	 */
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {

		return getIdPrimitives() //
				.map(it -> new ConvertiblePair(Identifier.class, it)) //
				.collect(Collectors.toUnmodifiableSet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		if (source == null) {
			return source;
		}

		Field idField = CACHE.computeIfAbsent(source.getClass(), type -> {
			return Arrays.stream(type.getDeclaredFields()) //
					.filter(it -> !Modifier.isStatic(it.getModifiers())) //
					.filter(it -> it.getType().equals(UUID.class)) //
					.peek(ReflectionUtils::makeAccessible) //
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("Unable to find UUID identifier field on " + type + "!"));
		});

		var id = ReflectionUtils.getField(idField, source);

		return targetType.getType().equals(UUID.class) ? (UUID) id : id.toString();
	}
}
