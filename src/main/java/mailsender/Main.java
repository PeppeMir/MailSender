package mailsender;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.ApplicationModule;
import parsing.CSVParsingService;

public class Main {

	public static void main(final String[] args) {

		final Injector injector = Guice.createInjector(new ApplicationModule());
		final CSVParsingService parsingService = injector.getInstance(CSVParsingService.class);

		parsingService.parse();
	}
}
