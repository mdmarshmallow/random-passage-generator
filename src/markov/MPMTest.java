package markov;

import java.io.IOException;

public class MPMTest {
	public static void main(String[] args) throws IOException {
		SecondOrderMarkovModel som = new SecondOrderMarkovModel("src/markov/test.corpus");
		/*FirstOrderMarkovModel fom = new FirstOrderMarkovModel("src/markov/test.corpus");
		System.out.println(fom.generatePassage(1000, 80));*/
	}
}
