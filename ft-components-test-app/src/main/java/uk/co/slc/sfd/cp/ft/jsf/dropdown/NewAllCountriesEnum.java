package uk.co.slc.sfd.cp.ft.jsf.dropdown;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum NewAllCountriesEnum implements LocalizedTextEnum, Dropdownable {

	UK("GB", "UnitedKingdom"), AFGHANISTAN("AF", "Afghanistan"), ALBANIA("AL", "Albania"), ALGERIA("DZ", "Algeria"), AMERICAN_SAMOA("AS",
			"AmericanSamoa"), ANDORRA("AD", "Andorra"), ANGOLA("AO", "Angola"), ANGUILLA("AI", "Anguilla"), ANTARCTICA("AQ", "Antarctica"), ANTIGUA_AND_BARBUDA(
			"AG", "AntiguaandBarbuda"), ARGENTINA("AR", "Argentina"), ARMENIA("AM", "Armenia"), ARUBA("AW", "Aruba"), AUSTRIA("AT",
			"Austria"), AUSTRALIA("AU", "Australia"), AZERBAIJAN("AZ", "Azerbaijan"), BAHAMAS("BS", "Bahamas"), BAHRAIN("BH", "Bahrain"), BANGLADESH(
			"BD", "Bangladesh"), BARBADOS("BB", "Barbados"), BELARUS("BY", "Belarus"), BELGIUM("BE", "Belgium"), BELIZE("BZ", "Belize"), BENIN(
			"BJ", "Benin"), BERMUDA("BM", "Bermuda"), BHUTAN("BT", "Bhutan"), BOLIVIA("BO", "Bolivia"), BOSNIA_AND_HERZEGOVINA("BA",
			"BosniaandHerzegovina"), BOTSWANA("BW", "Botswana"), BOUVET_ISLAND("BV", "BouvetIsland"), BRAZIL("BR", "Brazil"), BRITISH_INDIAN_OCEAN_TERRITORY(
			"IO", "BritishIndianOceanTerritory"), BRUNEI_DARUSSALAM("BN", "BruneiDarussalam"), BULGARIA("BG", "Bulgaria"), BURKINA_FASO(
			"BF", "BurkinaFaso"), BURUNDI("BI", "Burundi"), CAMBODIA("KH", "Cambodia"), CAMEROON("CM", "Cameroon"), CANADA("CA", "Canada"), CAPE_VERDE(
			"CV", "CapeVerde"), CAYMAN_ISLANDS("KY", "CaymanIslands"), CENTRAL_AFRICAN_REPUBLIC("CF", "CentralAfricanRepublic"), CHAD("TD",
			"Chad"), CHILE("CL", "Chile"), CHINA("CN", "China"), CHRISTMAS_ISLAND("CX", "ChristmasIsland"),

	COCOS_KEELING_ISLANDS("CC", "CocosKeelingIslands"), COLOMBIA("CO", "Colombia"), COMOROS("KM", "Comoros"), CONGO("CG", "Congo"), CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE(
			"CD", "Congo,TheDemocraticRepublicofthe"), COOK_ISLANDS("CK", "CookIslands"), COSTA_RICA("CR", "CostaRica"), COTE_DIVOIRE("CI",
			"Coted'Ivoire"), CROATIA("HR", "Croatia"), CUBA("CU", "Cuba"), CYPRUS("CY", "Cyprus"), CZECH_REPUBLIC("CZ", "CzechRepublic"), DENMARK(
			"DK", "Denmark"), DJIBOUTI("DJ", "Djibouti"), DOMINICA("DM", "Dominica"), DOMINICAN_REPUBLIC("DO", "DominicanRepublic"), ECUADOR(
			"EC", "Ecuador"), EGYPT("EG", "Egypt"), EL_SALVADOR("SV", "ElSalvador"), EQUATORIAL_GUINEA("GQ", "EquatorialGuinea"), ERITREA(
			"ER", "Eritrea"), ETHIOPIA("ET", "Ethiopia"), ESTONIA("EE", "Estonia"), FALKLAND_ISLANDS_MALVINAS("FK",
			"FalklandIslandsMalvinas"), FAROE_ISLANDS("FO", "FaroeIslands"), FIJI("FJ", "Fiji"), FINLAND("FI", "Finland"), FRANCE("FR",
			"France"), FRENCH_GUIANA("GF", "FrenchGuiana"), FRENCH_POLYNESIA("PF", "FrenchPolynesia"), FRENCH_SOUTHERN_TERRITORIES("TF",
			"FrenchSouthernTerritories"), GABON("GA", "Gabon"), GAMBIA("GM", "Gambia"), GEORGIA("GE", "Georgia"), GERMANY("DE", "Germany"), GHANA(
			"GH", "Ghana"), GIBRALTAR("GI", "Gibraltar"), GREECE("GR", "Greece"), GREENLAND("GL", "Greenland"), GRENADA("GD", "Grenada"), GUADELOUPE(
			"GP", "Guadeloupe"), GUAM("GU", "Guam"), GUATEMALA("GT", "Guatemala"), GUINEA("GN", "Guinea"), GUINEA_BISSAU("GW",
			"Guinea-Bissau"), GUYANA("GY", "Guyana"), HAITI("HT", "Haiti"), HEARD_ISLAND_AND_MCDONALD_ISLANDS("HM",
			"HeardIslandandMcDonaldIslands"), HOLY_SEE_VATICAN_CITY_STATE("VA", "HolySeeVaticanCityState"), HONDURAS("HN", "Honduras"), HONG_KONG(
			"HK", "HongKong"), HUNGARY("HU", "Hungary"), ICELAND("IS", "Iceland"), INDIA("IN", "India"), INDONESIA("ID", "Indonesia"), IRAN_ISLAMIC_REPUBLIC_OF(
			"IR", "IranIslamicRepublicof"), IRAQ("IQ", "Iraq"), ISRAEL("IL", "Israel"), ITALY("IT", "Italy"), JAMAICA("JM", "Jamaica"), JAPAN(
			"JP", "Japan"), JORDAN("JO", "Jordan"), KAZAKHSTAN("KZ", "Kazakhstan"), KENYA("KE", "Kenya"), KIRIBATI("KI", "Kiribati"), KOREA_DEMOCRATIC_PEOPLES_REPUBLIC_OF(
			"KP", "Korea,DemocraticPeople'sRepublicof"), KOREA_REPUBLIC_OF("KR", "Korea,Republicof"), KUWAIT("KW", "Kuwait"), KYRGYZSTAN(
			"KG", "Kyrgyzstan"), LAO_PEOPLES_DEMOCRATIC_REPUBLIC("LA", "LaoPeople'sDemocraticRepublic"), LATVIA("LV", "Latvia"), LEBANON(
			"LB", "Lebanon"), LESOTHO("LS", "Lesotho"), LIBERIA("LR", "Liberia"), LIBYAN_ARAB_JAMAHIRIYA("LY", "LibyanArabJamahiriya"), LIECHTENSTEIN(
			"LI", "Liechtenstein"), LITHUANIA("LT", "Lithuania"), LUXEMBOURG("LU", "Luxembourg"), MACAO("MO", "Macao"), MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF(
			"MK", "Macedonia,TheFormerYugoslavRepublicof"), MADAGASCAR("MG", "Madagascar"), MALAWI("MW", "Malawi"), MALAYSIA("MY",
			"Malaysia"), MALDIVES("MV", "Maldives"), MALI("ML", "Mali"), MALTA("MT", "Malta"), MARSHALL_ISLANDS("MH", "MarshallIslands"), MARTINIQUE(
			"MQ", "Martinique"), MAURITANIA("MR", "Mauritania"), MAURITIUS("MU", "Mauritius"), MAYOTTE("YT", "Mayotte"), MEXICO("MX",
			"Mexico"), MICRONESIA_FEDERATED_STATES_OF("FM", "MicronesiaFederatedStatesof"), MOLDOVA_REPUBLIC_OF("MD", "Moldova,Republicof"), MONACO(
			"MC", "Monaco"), MONGOLIA("MN", "Mongolia"), MONTSERRAT("MS", "Montserrat"), MOROCCO("MA", "Morocco"), MOZAMBIQUE("MZ",
			"Mozambique"), MYANMAR("MM", "Myanmar"), NAMIBIA("NA", "Namibia"), NAURU("NR", "Nauru"), NEPAL("NP", "Nepal"), NETHERLANDS(
			"NL", "Netherlands"), NETHERLANDS_ANTILLES("AN", "NetherlandsAntilles"), NEW_CALEDONIA("NC", "NewCaledonia"), NEW_ZEALAND("NZ",
			"NewZealand"), NICARAGUA("NI", "Nicaragua"), NIGER("NE", "Niger"), NIGERIA("NG", "Nigeria"), NIUE("NU", "Niue"), NORFOLK_ISLAND(
			"NF", "NorfolkIsland"), NORTHERN_MARIANA_ISLANDS("MP", "NorthernMarianaIslands"), NORWAY("NO", "Norway"), OMAN("OM", "Oman"), PAKISTAN(
			"PK", "Pakistan"), PALAU("PW", "Palau"), PALESTINIAN_TERRITORY_OCCUPIED("PS", "PalestinianTerritory,Occupied"), PANAMA("PA",
			"Panama"), PAPUA_NEW_GUINEA("PG", "PapuaNewGuinea"), PARAGUAY("PY", "Paraguay"), PERU("PE", "Peru"), PHILIPPINES("PH",
			"Philippines"), PITCAIRN("PN", "Pitcairn"), POLAND("PL", "Poland"), PORTUGAL("PT", "Portugal"), PUERTO_RICO("PR", "PuertoRico"), QATAR(
			"QA", "Qatar"), REPUBLIC_OF_IRELAND("IE", "RepublicofIreland"), REUNION("RE", "Reunion"), ROMANIA("RO", "Romania"), RUSSIAN_FEDERATION(
			"RU", "RussianFederation"), RWANDA("RW", "Rwanda"), SAINT_HELENA("SH", "SaintHelena"), SAINT_KITTS_AND_NEVIS("KN",
			"SaintKittsandNevis"), SAINT_LUCIA("LC", "SaintLucia"), SAINT_PIERRE_AND_MIQUELON("PM", "SaintPierreandMiquelon"), SAINT_VINCENT_AND_THE_GRENADINES(
			"VC", "SaintVincentandtheGrenadines"), SAMOA("WS", "Samoa"), SAN_MARINO("SM", "SanMarino"), SAO_TOME_AND_PRINCIPE("ST",
			"SaoTomeandPrincipe"), SAUDI_ARABIA("SA", "SaudiArabia"), SENEGAL("SN", "Senegal"), SERBIA_AND_MONTENEGRO("CS",
			"SerbiaandMontenegro"), SEYCHELLES("SC", "Seychelles"), SIERRA_LEONE("SL", "SierraLeone"), SINGAPORE("SG", "Singapore"), SLOVAKIA(
			"SK", "Slovakia"), SLOVENIA("SI", "Slovenia"), SOLOMON_ISLANDS("SB", "SolomonIslands"), SOMALIA("SO", "Somalia"), SOUTH_AFRICA(
			"ZA", "SouthAfrica"), SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLAND("GS", "SouthGeorgiaandtheSouthSandwichIsland"), SPAIN("ES",
			"Spain"), SRI_LANKA("LK", "SriLanka"), SUDAN("SD", "Sudan"), SURINAME("SR", "Suriname"), SVALBARD_AND_JAN_MAYEN_ISLANDS("SJ",
			"SvalbardandJanMayenIslands"), SWEDEN("SE", "Sweden"), SWAZILAND("SZ", "Swaziland"), SWITZERLAND("CH", "Switzerland"), SYRIAN_ARAB_REPUBLIC(
			"SY", "SyrianArabRepublic"), TAIWAN("TW", "Taiwan"), TAJIKISTAN("TJ", "Tajikistan"), TANZANIA_UNITED_REPUBLIC_OF("TZ",
			"Tanzania,UnitedRepublicof"), THAILAND("TH", "Thailand"), TIMOR_LESTE("TL", "Timor-Leste"), TOGO("TG", "Togo"), TOKELAU("TK",
			"Tokelau"), TONGA("TO", "Tonga"), TRINIDAD_AND_TOBAGO("TT", "TrinidadandTobago"), TUNISIA("TN", "Tunisia"), TURKEY("TR",
			"Turkey"), TURKMENISTAN("TM", "Turkmenistan"), TURKS_AND_CAICOS_ISLANDS("TC", "TurksandCaicosIslands"), TUVALU("TV", "Tuvalu"), UGANDA(
			"UG", "Uganda"), UKRAINE("UA", "Ukraine"), UNITED_ARAB_EMIRATES("AE", "UnitedArabEmirates"), UNITED_STATES("US", "UnitedStates"), UNITED_STATES_MINOR_OUTLYING_ISLANDS(
			"UM", "UnitedStatesMinorOutlyingIslands"), URUGUAY("UY", "Uruguay"), UZBEKISTAN("UZ", "Uzbekistan"), VANUATU("VU", "Vanuatu"), VENEZUELA(
			"VE", "Venezuela"), VIET_NAM("VN", "VietNam"), VIRGIN_ISLANDS_BRITISH("VG", "VirginIslands,British"), VIRGIN_ISLANDS_US("VI",
			"VirginIslands,U.S."), WALLIS_AND_FUTUNA("WF", "WallisandFutuna"), WESTERN_SAHARA("EH", "WesternSahara"), YEMEN("YE", "Yemen"), YUGOSLAVIA(
			"YU", "Yugoslavia"), ZAMBIA("ZM", "Zambia"), ZIMBABWE("ZW", "Zimbabwe");

	private String code;
	private String label;

	private NewAllCountriesEnum(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public static List<NewAllCountriesEnum> getAsList() {
		return Arrays.asList(NewAllCountriesEnum.class.getEnumConstants());
	}

	@Override
	public String getLocalizedText() {
		return LocalizedTextEnumHelper.getLocalizedText(this);
	}

	@Override
	public String getLocalizedText(Locale loc) {
		return LocalizedTextEnumHelper.getLocalizedText(this, loc);
	}

	@Override
	public String getKeyForText() {
		return code;
	}

	@Override
	public Object getKey() {
		return getCode();
	}

	@Override
	public String getDropdownValue() {
		return getLocalizedText();
	}
	
	public static NewAllCountriesEnum findByName(String code) {
		for (NewAllCountriesEnum allCountries : NewAllCountriesEnum.values()) {
			if (allCountries.getCode().contains(code)) {
				return allCountries;
			}
		}
		return null;
	}

	@Override
	public String getDropdownValueForLocale(Locale locale) {
		return getLocalizedText(locale);
	}


}
