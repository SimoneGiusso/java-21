package com.simonegiusso.java21.sequencedCollections;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

@Slf4j
public class SequencedCollectionsTest {

    SequencedCollection<String> sequencedCollection;
    SequencedSet<?> sequencedSet;
    SequencedMap<?, ?> sequencedMap;

    /*
        // New Methods to create them
        Collections.unmodifiableSequencedCollection(sequencedCollection);
        Collections.unmodifiableSequencedSet(sequencedSet);
        Collections.unmodifiableSequencedMap(sequencedMap);
     */

    @BeforeEach
    void setUp() {
        sequencedCollection = new ArrayList<>(List.of("J", "a", "v", "a", "-", "2", "1"));
    }

    @Test
    void testReversed() {
        SequencedCollection<String> reversedSequencedCollection = sequencedCollection.reversed();

        sequencedCollection.addLast("<3");

        // The reversed operator returns a view. Modification to the original sequence are reflected to this view
        Assertions.assertEquals("<3", reversedSequencedCollection.getFirst());

        reversedSequencedCollection.addFirst("!");

        // Change on the view are reflected on the original sequence
        Assertions.assertEquals("!", sequencedCollection.getLast());

        log.debug(String.valueOf(sequencedCollection));
    }

}