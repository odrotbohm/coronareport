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
package quarano.tracking.web;

import de.wevsvirushackathon.coronareport.client.Client;
import quarano.reference.NewSymptomRepository;
import quarano.tracking.Address.HouseNumber;
import quarano.tracking.BodyTemperature;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.DiaryEntry;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.TrackedPerson;
import quarano.tracking.ZipCode;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Oliver Drotbohm
 */
@Component
public class MappingConfiguration {

	private static final Converter<String, EmailAddress> STRING_TO_EMAIL_ADDRESS //
			= source -> source.getSource() == null ? null : EmailAddress.of(source.getSource());

	private static final Converter<String, PhoneNumber> STRING_TO_PHONE_NUMBER //
			= source -> source.getSource() == null ? null : PhoneNumber.of(source.getSource());

	private static final Converter<String, ZipCode> STRING_TO_ZIP_CODE //
			= source -> source.getSource() == null ? null : ZipCode.of(source.getSource());

	private static final Converter<String, HouseNumber> STRING_TO_HOUSE_NUMBER //
			= source -> HouseNumber.of(source.getSource());

	private static final Converter<Float, BodyTemperature> FLOAT_TO_BODY_TEMPERATURE //
			= source -> source.getSource() == null ? null : BodyTemperature.of(source.getSource());

	private static final Converter<BodyTemperature, Float> BODY_TEMPERATURE_TO_FLOAT //
			= source -> source.getSource().getValue();

	public MappingConfiguration(ModelMapper mapper, NewSymptomRepository symptoms, ContactPersonRepository contacts) {

		mapper.getConfiguration().setMethodAccessLevel(AccessLevel.PACKAGE_PRIVATE);

		mapper.addConverter(STRING_TO_EMAIL_ADDRESS, String.class, EmailAddress.class);
		mapper.addConverter(STRING_TO_PHONE_NUMBER, String.class, PhoneNumber.class);
		mapper.addConverter(STRING_TO_ZIP_CODE, String.class, ZipCode.class);
		mapper.addConverter(STRING_TO_HOUSE_NUMBER, String.class, HouseNumber.class);

		mapper.addConverter(BODY_TEMPERATURE_TO_FLOAT, BodyTemperature.class, float.class);
		mapper.addConverter(FLOAT_TO_BODY_TEMPERATURE, float.class, BodyTemperature.class);

		mapper.typeMap(de.wevsvirushackathon.coronareport.contactperson.ContactPerson.class, ContactPerson.class)
				.setPreConverter(context -> {

					var source = context.getSource();
					return new ContactPerson(source.getFirstname(), source.getSurename());

				});

		mapper.typeMap(ContactPersonDto.class, ContactPerson.class).setPreConverter(context -> {

			var source = context.getSource();

			return context.getDestination() != null //
					? context.getDestination() //
					: new ContactPerson(source.getFirstName(), source.getLastName());
		}).addMappings(it -> {

			it.using(STRING_TO_PHONE_NUMBER).map(ContactPersonDto::getMobilePhone, ContactPerson::setMobilePhoneNumber);
			it.using(STRING_TO_PHONE_NUMBER).map(ContactPersonDto::getPhone, ContactPerson::setPhoneNumber);
			it.using(STRING_TO_EMAIL_ADDRESS).map(ContactPersonDto::getEmail, ContactPerson::setEmailAddress);
			it.using(STRING_TO_HOUSE_NUMBER).<HouseNumber> map(ContactPersonDto::getHouseNumber,
					(target, v) -> target.getAddress().setHouseNumber(v));

			it.<String> map(ContactPersonDto::getStreet, (target, v) -> target.getAddress().setStreet(v));
			it.<String> map(ContactPersonDto::getCity, (target, v) -> target.getAddress().setCity(v));
			it.<ZipCode> map(ContactPersonDto::getZipCode, (target, v) -> target.getAddress().setZipCode(v));
		});

		mapper.typeMap(ContactPerson.class, ContactPersonDto.class).addMappings(it -> {

			it.map(source -> source.getAddress().getStreet(), ContactPersonDto::setStreet);
			it.map(source -> source.getAddress().getZipCode(), ContactPersonDto::setZipCode);
			it.map(source -> source.getAddress().getCity(), ContactPersonDto::setCity);
			it.map(source -> source.getAddress().getHouseNumber(), ContactPersonDto::setHouseNumber);
		});

		mapper.typeMap(Client.class, TrackedPerson.class).setPreConverter(context -> {

			var source = context.getSource();
			return new TrackedPerson(source.getFirstname(), source.getSurename());

		}).addMappings(it -> {
			it.map(Client::getClientId, TrackedPerson::setLegacyClientId);
		});

		mapper.typeMap(TrackedPerson.class, TrackedPersonDto.class).addMappings(it -> {

			it.map(source -> source.getAddress().getStreet(), TrackedPersonDto::setStreet);
			it.map(source -> source.getAddress().getZipCode(), TrackedPersonDto::setZipCode);
			it.map(source -> source.getAddress().getCity(), TrackedPersonDto::setCity);
			it.map(source -> source.getAddress().getHouseNumber(), TrackedPersonDto::setHouseNumber);
		});

		mapper.typeMap(TrackedPersonDto.class, TrackedPerson.class).addMappings(it -> {

			it.using(STRING_TO_PHONE_NUMBER).map(TrackedPersonDto::getMobilePhone, TrackedPerson::setMobilePhoneNumber);
			it.using(STRING_TO_PHONE_NUMBER).map(TrackedPersonDto::getPhone, TrackedPerson::setPhoneNumber);
			it.using(STRING_TO_EMAIL_ADDRESS).map(TrackedPersonDto::getEmail, TrackedPerson::setEmailAddress);
			it.using(STRING_TO_HOUSE_NUMBER).<HouseNumber> map(TrackedPersonDto::getHouseNumber,
					(target, v) -> target.getAddress().setHouseNumber(v));

			it.<String> map(TrackedPersonDto::getStreet, (target, v) -> target.getAddress().setStreet(v));
			it.<String> map(TrackedPersonDto::getCity, (target, v) -> target.getAddress().setCity(v));
			it.<ZipCode> map(TrackedPersonDto::getZipCode, (target, v) -> target.getAddress().setZipCode(v));
		});

		mapper.typeMap(DiaryEntryDto.class, DiaryEntry.class).setPreConverter(context -> {

			var source = context.getSource();

			return DiaryEntry.of(null, source.getDate());

		});
	}
}
