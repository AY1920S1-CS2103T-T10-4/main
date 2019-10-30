package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;


public class SuggestingCommandUtilTest {
    @Test
    void createFuzzyMatcher_exactMatch_success() {
        final String sequence = "mdm";
        final String passingMatch = sequence;
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_oneCharacterBetweenNoTrailing_success() {
        final String sequence = "mdm";
        final String passingMatch = "madam";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_trailingCharacter_success() {
        final String sequence = "mdm";
        final String passingMatch = sequence + "a";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_oneCharacterBetweenWithTrailing_success() {
        final String sequence = "mdm";
        final String passingMatch = "madame";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_manyCharactersBetweenNoTrailing_success() {
        final String sequence = "mdm";
        final String passingMatch = "medium";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_manyCharactersBetweenWithTrailing_success() {
        final String sequence = "mdm";
        final String passingMatch = "madman";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_matchUnicodeSurrogatePair_success() {
        final String sequence = "😁😁";
        final String passingMatch = "😁smile😁";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_trailingNonLatinCharacters_success() {
        final String sequence = "mdm";
        final String passingMatch = "mdm你好";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_incompleteMatch_fail() {
        final String sequence = "mdm";
        final String failingMatch = "md";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertFalse(predicate.test(failingMatch));
    }

    @Test
    void createFuzzyMatcher_leadingCharacter_fail() {
        final String sequence = "mdm";
        final String failingMatch = "a" + sequence;
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertFalse(predicate.test(failingMatch));
    }

    @Test
    void createFuzzyMatcher_spaceInBetween_fail() {
        final String sequence = "mdm";
        final String failingMatch = "m dm";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertFalse(predicate.test(failingMatch));
    }

    @Test
    void createFuzzyMatcher_trailingSpace_fail() {
        final String sequence = "mdm";
        final String passingMatch = sequence + " ";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertFalse(predicate.test(passingMatch));
    }

    @Test
    void createFuzzyMatcher_dashCharacter_success() {
        final String sequence = "tsg";
        final String passingMatch = "test-string";
        final Predicate<String> predicate = SuggestingCommandUtil.createFuzzyMatcher(sequence);

        assertTrue(predicate.test(passingMatch));
    }
}
