package support;

import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.DescriptionParser;

public class DescriptionParserTest {
	
	

	public static void main(String[] args) {
		DescriptionParser parser = new DescriptionParser();
		String completeSource = "A single-line paragraph.$br$\n" +
									"Some tabbed$t$text.$n$\n" +
									"Text\n" +
									"which will be on the same line.$n$\n" +
									"But this not.$br$\n" +
									"$1$ First argument$n$\n" +
									"$1$ Second$n$\n" +
									"$1$ Last$br$\n" +
									"By the way:\n" +
									"$1$ Other first argument$br$";
		System.out.println(parser.parse("A single-line paragraph.$br$\nSome tabbed$t$Text."));
		System.out.println("---\nComplex Test:");
		System.out.println(parser.parse(completeSource));

	}

}
