package io.start.biruk.saveit.util;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.List;

/**
 * Created by biruk on 5/10/2018.
 */
public class StringUtil {

    private static final String TAG_JOINER_SEQ = "__";
    private static final Joiner TAG_JOINER = Joiner.on(TAG_JOINER_SEQ).skipNulls();

    public static List<String> getTagList(String tag) {
        List<String> tagList = Stream.of(Splitter.on(TAG_JOINER_SEQ)
                .trimResults()
                .omitEmptyStrings()
                .split(tag)
        ).toList();

        return tagList;
    }

    public static String appendTag(String oldTag, String newTag) {
        if (oldTag.isEmpty()) {
            return newTag;
        } else {
            String tag = TAG_JOINER.join(oldTag, newTag);
            return tag;
        }
    }

    public static String getFormatedTag(String tag) {
        return tag.replace(TAG_JOINER_SEQ, ",");
    }

}
