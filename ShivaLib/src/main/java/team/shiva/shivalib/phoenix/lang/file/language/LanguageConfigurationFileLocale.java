package team.shiva.shivalib.phoenix.lang.file.language;

public enum LanguageConfigurationFileLocale {
    ENGLISH("en"),
    EXPLICIT("ex"),
    FRENCH("fr"),
    SPANISH("es"),
    PORTUGUESE("pt");

    private final String abbreviation;

    LanguageConfigurationFileLocale(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static LanguageConfigurationFileLocale getByAbbreviation(String abbreviation) {
        for (LanguageConfigurationFileLocale locale : LanguageConfigurationFileLocale.values()) {
            if (!locale.getAbbreviation().equalsIgnoreCase(abbreviation)) continue;
            return locale;
        }
        return ENGLISH;
    }

    public static LanguageConfigurationFileLocale getByName(String name) {
        for (LanguageConfigurationFileLocale locale : LanguageConfigurationFileLocale.values()) {
            if (!locale.name().equalsIgnoreCase(name)) continue;
            return locale;
        }
        return ENGLISH;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }
}

