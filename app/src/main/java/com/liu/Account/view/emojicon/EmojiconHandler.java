/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liu.Account.view.emojicon;

import android.content.Context;
import android.text.Spannable;
import android.util.SparseIntArray;

import com.liu.Account.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public final class EmojiconHandler {
    private EmojiconHandler() {
    }

    private static final SparseIntArray sEmojisMap = new SparseIntArray(1029);
    private static final SparseIntArray sSoftbanksMap = new SparseIntArray(471);
    private static Map<String, Integer> sEmojisModifiedMap = new HashMap<>();

    static {
        // People
        sEmojisMap.put(0x1f604, R.drawable.emoji_1f604);
        sEmojisMap.put(0x1f603, R.drawable.emoji_1f603);
        sEmojisMap.put(0x1f600, R.drawable.emoji_1f600);
        sEmojisMap.put(0x1f60a, R.drawable.emoji_1f60a);
        sEmojisMap.put(0x263a, R.drawable.emoji_263a);
        sEmojisMap.put(0x1f609, R.drawable.emoji_1f609);
        sEmojisMap.put(0x1f60d, R.drawable.emoji_1f60d);
        sEmojisMap.put(0x1f618, R.drawable.emoji_1f618);
        sEmojisMap.put(0x1f61a, R.drawable.emoji_1f61a);
        sEmojisMap.put(0x1f617, R.drawable.emoji_1f617);
        sEmojisMap.put(0x1f619, R.drawable.emoji_1f619);
        sEmojisMap.put(0x1f61c, R.drawable.emoji_1f61c);
        sEmojisMap.put(0x1f61d, R.drawable.emoji_1f61d);
        sEmojisMap.put(0x1f61b, R.drawable.emoji_1f61b);
        sEmojisMap.put(0x1f633, R.drawable.emoji_1f633);
        sEmojisMap.put(0x1f601, R.drawable.emoji_1f601);
        sEmojisMap.put(0x1f614, R.drawable.emoji_1f614);
        sEmojisMap.put(0x1f60c, R.drawable.emoji_1f60c);
        sEmojisMap.put(0x1f612, R.drawable.emoji_1f612);
        sEmojisMap.put(0x1f61e, R.drawable.emoji_1f61e);
        sEmojisMap.put(0x1f623, R.drawable.emoji_1f623);
        sEmojisMap.put(0x1f622, R.drawable.emoji_1f622);
        sEmojisMap.put(0x1f602, R.drawable.emoji_1f602);
        sEmojisMap.put(0x1f62d, R.drawable.emoji_1f62d);
        sEmojisMap.put(0x1f62a, R.drawable.emoji_1f62a);
        sEmojisMap.put(0x1f625, R.drawable.emoji_1f625);
        sEmojisMap.put(0x1f630, R.drawable.emoji_1f630);
        sEmojisMap.put(0x1f605, R.drawable.emoji_1f605);
        sEmojisMap.put(0x1f613, R.drawable.emoji_1f613);
        sEmojisMap.put(0x1f629, R.drawable.emoji_1f629);
        sEmojisMap.put(0x1f62b, R.drawable.emoji_1f62b);
        sEmojisMap.put(0x1f628, R.drawable.emoji_1f628);
        sEmojisMap.put(0x1f631, R.drawable.emoji_1f631);
        sEmojisMap.put(0x1f620, R.drawable.emoji_1f620);
        sEmojisMap.put(0x1f621, R.drawable.emoji_1f621);
        sEmojisMap.put(0x1f624, R.drawable.emoji_1f624);
        sEmojisMap.put(0x1f616, R.drawable.emoji_1f616);
        sEmojisMap.put(0x1f606, R.drawable.emoji_1f606);
        sEmojisMap.put(0x1f60b, R.drawable.emoji_1f60b);
        sEmojisMap.put(0x1f637, R.drawable.emoji_1f637);
        sEmojisMap.put(0x1f60e, R.drawable.emoji_1f60e);
        sEmojisMap.put(0x1f634, R.drawable.emoji_1f634);
        sEmojisMap.put(0x1f635, R.drawable.emoji_1f635);
        sEmojisMap.put(0x1f632, R.drawable.emoji_1f632);
        sEmojisMap.put(0x1f61f, R.drawable.emoji_1f61f);
        sEmojisMap.put(0x1f626, R.drawable.emoji_1f626);
        sEmojisMap.put(0x1f627, R.drawable.emoji_1f627);
        sEmojisMap.put(0x1f608, R.drawable.emoji_1f608);
        sEmojisMap.put(0x1f47f, R.drawable.emoji_1f47f);
        sEmojisMap.put(0x1f62e, R.drawable.emoji_1f62e);
        sEmojisMap.put(0x1f62c, R.drawable.emoji_1f62c);
        sEmojisMap.put(0x1f610, R.drawable.emoji_1f610);
        sEmojisMap.put(0x1f615, R.drawable.emoji_1f615);
        sEmojisMap.put(0x1f62f, R.drawable.emoji_1f62f);
        sEmojisMap.put(0x1f636, R.drawable.emoji_1f636);
        sEmojisMap.put(0x1f607, R.drawable.emoji_1f607);
        sEmojisMap.put(0x1f60f, R.drawable.emoji_1f60f);
        sEmojisMap.put(0x1f611, R.drawable.emoji_1f611);
        sEmojisMap.put(0x1f63a, R.drawable.emoji_1f63a);
        sEmojisMap.put(0x1f638, R.drawable.emoji_1f638);
        sEmojisMap.put(0x1f63b, R.drawable.emoji_1f63b);
        sEmojisMap.put(0x1f63d, R.drawable.emoji_1f63d);
        sEmojisMap.put(0x1f63c, R.drawable.emoji_1f63c);
        sEmojisMap.put(0x1f640, R.drawable.emoji_1f640);
        sEmojisMap.put(0x1f63f, R.drawable.emoji_1f63f);
        sEmojisMap.put(0x1f639, R.drawable.emoji_1f639);
        sEmojisMap.put(0x1f63e, R.drawable.emoji_1f63e);
        sEmojisMap.put(0x1f64a, R.drawable.emoji_1f64a);
        sEmojisMap.put(0x1f443, R.drawable.emoji_1f443);
        sEmojisMap.put(0x1f444, R.drawable.emoji_1f444);
        sEmojisMap.put(0x1f450, R.drawable.emoji_1f450);
        sEmojisMap.put(0x1f446, R.drawable.emoji_1f446);
        sEmojisMap.put(0x1f447, R.drawable.emoji_1f447);
        sEmojisMap.put(0x1f449, R.drawable.emoji_1f449);
        sEmojisMap.put(0x1f448, R.drawable.emoji_1f448);
        sEmojisMap.put(0x1f64c, R.drawable.emoji_1f64c);
        sEmojisMap.put(0x1f64f, R.drawable.emoji_1f64f);
        sEmojisMap.put(0x1f64b, R.drawable.emoji_1f64b);
        sEmojisMap.put(0x1f64e, R.drawable.emoji_1f64e);
        sEmojisMap.put(0x1f64d, R.drawable.emoji_1f64d);


        sEmojisMap.put(0x1f47b, R.drawable.emoji_1f47b);

        sEmojisMap.put(0x1f48a, R.drawable.emoji_1f48a);

        sEmojisMap.put(0x1f68a, R.drawable.emoji_1f68a);

        sEmojisMap.put(0x1f68b, R.drawable.emoji_1f68b);
        sEmojisMap.put(0x1f68e, R.drawable.emoji_1f68e);
        sEmojisMap.put(0x1f68c, R.drawable.emoji_1f68c);
        sEmojisMap.put(0x1f68d, R.drawable.emoji_1f68d);
        sEmojisMap.put(0x1f699, R.drawable.emoji_1f699);
        sEmojisMap.put(0x1f698, R.drawable.emoji_1f698);
//        Emoji.fromChars("\ud83c\uddef\ud83c\uddf5");
//        Emoji.fromChars("\ud83c\uddf0\ud83c\uddf7");
//        Emoji.fromChars("\ud83c\udde9\ud83c\uddea");
//        Emoji.fromChars("\ud83c\udde8\ud83c\uddf3");
//        Emoji.fromChars("\ud83c\uddfa\ud83c\uddf8");
//        Emoji.fromChars("\ud83c\uddeb\ud83c\uddf7");
//        Emoji.fromChars("\ud83c\uddea\ud83c\uddf8");
//        Emoji.fromChars("\ud83c\uddee\ud83c\uddf9");
//        Emoji.fromChars("\ud83c\uddf7\ud83c\uddfa");
//        Emoji.fromChars("\ud83c\uddec\ud83c\udde7");

        // Symbols
//        Emoji.fromChars("\u0031\u20e3"),
//        Emoji.fromChars("\u0032\u20e3"),
//        Emoji.fromChars("\u0033\u20e3"),
//        Emoji.fromChars("\u0034\u20e3"),
//        Emoji.fromChars("\u0035\u20e3"),
//        Emoji.fromChars("\u0036\u20e3"),
//        Emoji.fromChars("\u0037\u20e3"),
//        Emoji.fromChars("\u0038\u20e3"),
//        Emoji.fromChars("\u0039\u20e3"),
//        Emoji.fromChars("\u0030\u20e3"),

        sSoftbanksMap.put(0xe056, R.drawable.emoji_1f60a);
        sSoftbanksMap.put(0xe057, R.drawable.emoji_1f603);
        sSoftbanksMap.put(0xe058, R.drawable.emoji_1f61e);
        sSoftbanksMap.put(0xe059, R.drawable.emoji_1f620);
        sSoftbanksMap.put(0xe105, R.drawable.emoji_1f61c);
        sSoftbanksMap.put(0xe106, R.drawable.emoji_1f60d);
        sSoftbanksMap.put(0xe107, R.drawable.emoji_1f631);
        sSoftbanksMap.put(0xe108, R.drawable.emoji_1f613);
        sSoftbanksMap.put(0xe11a, R.drawable.emoji_1f47f);
        sSoftbanksMap.put(0xe11b, R.drawable.emoji_1f47b);
        sSoftbanksMap.put(0xe159, R.drawable.emoji_1f68c);
        sSoftbanksMap.put(0xe22e, R.drawable.emoji_1f446);
        sSoftbanksMap.put(0xe22f, R.drawable.emoji_1f447);
        sSoftbanksMap.put(0xe230, R.drawable.emoji_1f448);
        sSoftbanksMap.put(0xe231, R.drawable.emoji_1f449);
        sSoftbanksMap.put(0xe30f, R.drawable.emoji_1f48a);
        sSoftbanksMap.put(0xe401, R.drawable.emoji_1f625);
        sSoftbanksMap.put(0xe402, R.drawable.emoji_1f60f);
        sSoftbanksMap.put(0xe403, R.drawable.emoji_1f614);
        sSoftbanksMap.put(0xe404, R.drawable.emoji_1f601);
        sSoftbanksMap.put(0xe405, R.drawable.emoji_1f609);
        sSoftbanksMap.put(0xe406, R.drawable.emoji_1f623);
        sSoftbanksMap.put(0xe407, R.drawable.emoji_1f616);
        sSoftbanksMap.put(0xe408, R.drawable.emoji_1f62a);
        sSoftbanksMap.put(0xe40a, R.drawable.emoji_1f606);
        sSoftbanksMap.put(0xe40b, R.drawable.emoji_1f628);
        sSoftbanksMap.put(0xe40c, R.drawable.emoji_1f637);
        sSoftbanksMap.put(0xe40d, R.drawable.emoji_1f633);
        sSoftbanksMap.put(0xe40e, R.drawable.emoji_1f612);
        sSoftbanksMap.put(0xe40f, R.drawable.emoji_1f630);
        sSoftbanksMap.put(0xe410, R.drawable.emoji_1f632);
        sSoftbanksMap.put(0xe411, R.drawable.emoji_1f62d);
        sSoftbanksMap.put(0xe412, R.drawable.emoji_1f602);
        sSoftbanksMap.put(0xe413, R.drawable.emoji_1f622);
        sSoftbanksMap.put(0xe414, R.drawable.emoji_263a);
        sSoftbanksMap.put(0xe415, R.drawable.emoji_1f605);
        sSoftbanksMap.put(0xe416, R.drawable.emoji_1f621);
        sSoftbanksMap.put(0xe417, R.drawable.emoji_1f61a);
        sSoftbanksMap.put(0xe418, R.drawable.emoji_1f618);
        sSoftbanksMap.put(0xe41a, R.drawable.emoji_1f443);
        sSoftbanksMap.put(0xe41c, R.drawable.emoji_1f444);
        sSoftbanksMap.put(0xe41d, R.drawable.emoji_1f64f);
        sSoftbanksMap.put(0xe422, R.drawable.emoji_1f450);
        sSoftbanksMap.put(0xe427, R.drawable.emoji_1f64c);
        sSoftbanksMap.put(0xe42e, R.drawable.emoji_1f699);

        sEmojisMap.put(0x1f642, R.drawable.emoji_1f642);
        sEmojisMap.put(0x1f917, R.drawable.emoji_1f917);
        sEmojisMap.put(0x1f914, R.drawable.emoji_1f914);
        sEmojisMap.put(0x1f644, R.drawable.emoji_1f644);
        sEmojisMap.put(0x1f910, R.drawable.emoji_1f910);
        sEmojisMap.put(0x1f913, R.drawable.emoji_1f913);
        sEmojisMap.put(0x1f641, R.drawable.emoji_1f641);
        sEmojisMap.put(0x1f643, R.drawable.emoji_1f643);
        sEmojisMap.put(0x1f912, R.drawable.emoji_1f912);
        sEmojisMap.put(0x1f915, R.drawable.emoji_1f915);
        sEmojisMap.put(0x1f911, R.drawable.emoji_1f911);
        sEmojisMap.put(0x1f916, R.drawable.emoji_1f916);
    }

    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }

    private static int getEmojiResource(Context context, int codePoint) {
        return sEmojisMap.get(codePoint);
    }

    private static int getSoftbankEmojiResource(char c) {
        return sSoftbanksMap.get(c);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, 0, -1, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param index
     * @param length
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, int index, int length) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, index, length, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, boolean useSystemDefault) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, 0, -1, useSystemDefault);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param index
     * @param length
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, int index, int length, boolean useSystemDefault) {
        if (useSystemDefault) {
            return;
        }

        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length + index);

        // remove spans throughout all text
        EmojiconSpan[] oldSpans = text.getSpans(0, textLength, EmojiconSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            text.removeSpan(oldSpans[i]);
        }

        int skip;
        for (int i = index; i < textLengthToProcess; i += skip) {
            skip = 0;
            int icon = 0;
            char c = text.charAt(i);
            if (isSoftBankEmoji(c)) {
                icon = getSoftbankEmojiResource(c);
                skip = icon == 0 ? 0 : 1;
            }

            if (icon == 0) {
                int unicode = Character.codePointAt(text, i);
                skip = Character.charCount(unicode);

                if (unicode > 0xff) {
                    icon = getEmojiResource(context, unicode);
                }

                if (i + skip < textLengthToProcess) {
                    int followUnicode = Character.codePointAt(text, i + skip);
                    //Non-spacing mark (Combining mark)
                    if (followUnicode == 0xfe0f) {
                        int followSkip = Character.charCount(followUnicode);
                        if (i + skip + followSkip < textLengthToProcess) {

                            int nextFollowUnicode = Character.codePointAt(text, i + skip + followSkip);
                            if (nextFollowUnicode == 0x20e3) {
                                int nextFollowSkip = Character.charCount(nextFollowUnicode);
                                int tempIcon = getKeyCapEmoji(unicode);

                                if (tempIcon == 0) {
                                    followSkip = 0;
                                    nextFollowSkip = 0;
                                } else {
                                    icon = tempIcon;
                                }
                                skip += (followSkip + nextFollowSkip);
                            }
                        }
                    } else if (followUnicode == 0x20e3) {
                        //some older versions of iOS don't use a combining character, instead it just goes straight to the second part
                        int followSkip = Character.charCount(followUnicode);

                        int tempIcon = getKeyCapEmoji(unicode);
                        if (tempIcon == 0) {
                            followSkip = 0;
                        } else {
                            icon = tempIcon;
                        }
                        skip += followSkip;

                    } else {
                        //handle other emoji modifiers
                        int followSkip = Character.charCount(followUnicode);

                        //TODO seems like we could do this for every emoji type rather than having that giant static map, maybe this is too slow?
                        String hexUnicode = Integer.toHexString(unicode);
                        String hexFollowUnicode = Integer.toHexString(followUnicode);

                        String resourceName = "emoji_" + hexUnicode + "_" + hexFollowUnicode;

                        int resourceId = 0;
                        if (sEmojisModifiedMap.containsKey(resourceName)) {
                            resourceId = sEmojisModifiedMap.get(resourceName);
                        } else {
                            resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getApplicationContext().getPackageName());
                            if (resourceId != 0) {
                                sEmojisModifiedMap.put(resourceName, resourceId);
                            }
                        }

                        if (resourceId == 0) {
                            followSkip = 0;
                        } else {
                            icon = resourceId;
                        }
                        skip += followSkip;
                    }
                }
            }

            if (icon > 0) {
                text.setSpan(new EmojiconSpan(context, icon, emojiSize, emojiAlignment, textSize), i, i + skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static int getKeyCapEmoji(int unicode) {
        int icon = 0;
        return icon;
    }

}
