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
package quarano.actions.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItemRepository;
import quarano.auth.web.LoggedIn;
import quarano.tracking.TrackedPerson;

import java.util.stream.Stream;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class ActionItemController {

	private final @NonNull ActionItemRepository items;
	private final @NonNull MessageSourceAccessor messages;

	@GetMapping("/api/actions")
	Stream<?> allActions(@LoggedIn TrackedPerson person) {

		return items.findByTrackedPerson(person.getId()) //
				.map(it -> ActionItemDto.of(it, messages)) //
				.stream();
	}
}
