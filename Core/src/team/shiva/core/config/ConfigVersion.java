package team.shiva.core.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.shiva.core.config.impl.ConfigConversion1;
import team.shiva.core.config.impl.ConfigConversion2;
import team.shiva.core.config.impl.ConfigConversion3;
import team.shiva.core.config.impl.ConfigConversion4;
import team.shiva.core.config.impl.ConfigConversion5;

@AllArgsConstructor
@Getter
public enum ConfigVersion {

	VERSION_1(1, new ConfigConversion1()),
	VERSION_2(2, new ConfigConversion2()),
	VERSION_3(3, new ConfigConversion3()),
	VERSION_4(4, new ConfigConversion4()),
	VERSION_5(5, new ConfigConversion5());

	private int number;
	private ConfigConversion conversion;

}
