package io.start.biruk.saveit.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * Created by biruk on 5/10/2018.
 */
public class StringUtil {

    private static final String  TAG_JOINER_SEQ="__";
    private static final Joiner TAG_JOINER=Joiner.on(TAG_JOINER_SEQ).skipNulls();

    public static Iterable<String> getTagList(String tag){
        return Splitter.on(TAG_JOINER_SEQ)
                .trimResults()
                .limit(4)
                .omitEmptyStrings()
                .split(tag);
    }

    public static String appendTag(String oldTag,String newTag){
        String tag = TAG_JOINER.join(oldTag, newTag);
        return tag;
    }
}
