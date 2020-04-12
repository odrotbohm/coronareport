package quarano.tracking.web;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Data
public class ContactPersonDto {

	@Setter(onMethod = @__(@JsonIgnore)) //
	@Getter(onMethod = @__(@JsonProperty)) //
	@JsonSerialize(using = ToStringSerializer.class) //
	private ContactPersonIdentifier id;

	private @NotEmpty String lastName, firstName;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @Pattern(regexp = EmailAddress.PATTERN) String email;
	private String remark;
	private String identificationHint;
	private Boolean isHealthStaff;
	private Boolean isSenior;
	private Boolean hasPreExistingConditions;
}
