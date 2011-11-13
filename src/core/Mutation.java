package core;

public enum Mutation {
	NEXT_STATE_MUTATION, ACTION_MUTATION, SIGNIFICANT_PREDICATE_MUTATION, START_STATE_MUTATION;

	public String getRuType() {
		switch (this) {
		case NEXT_STATE_MUTATION:
			return "номера следующего состояния";
		case ACTION_MUTATION:
			return "выходного воздействия";
		case SIGNIFICANT_PREDICATE_MUTATION:
			return "значимого предиката";
		case START_STATE_MUTATION:
			return "номера стартового состояния";
		}
		throw new RuntimeException();
	}
}
