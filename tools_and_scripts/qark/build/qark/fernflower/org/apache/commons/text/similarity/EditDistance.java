package org.apache.commons.text.similarity;

public interface EditDistance extends SimilarityScore {
   Object apply(CharSequence var1, CharSequence var2);
}
