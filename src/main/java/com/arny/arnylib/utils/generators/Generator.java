package com.arny.arnylib.utils.generators;


import com.arny.arnylib.utils.MathUtils;

import java.util.Random;
import java.util.UUID;

public class Generator {
    /*api*/
//    private static final String PLACEIMG_URL = "http://placeimg.com/250/250/arch";
    private static final String PLACEIMG_URL_TYPE_ANIMAL = "animals";
    private static final String PLACEIMG_URL_TYPE_ARCH = "arch";
    private static final String PLACEIMG_URL_TYPE_NATURE = "nature";
    private static final String PLACEIMG_URL_TYPE_PEOPLE = "people";
    private static final String PLACEIMG_URL_TYPE_TECH = "tech";
    private static final String LOREMPIXEL_URL_TYPE_ABSTRACT = "abstract";
    private static final String LOREMPIXEL_URL_TYPE_ANIMALS = "animals";
    private static final String LOREMPIXEL_URL_TYPE_BUSNESS = "business";
    private static final String LOREMPIXEL_URL_TYPE_CATS = "cats";
    private static final String LOREMPIXEL_URL_TYPE_CITY = "city";
    private static final String LOREMPIXEL_URL_TYPE_FOOD = "food";
    private static final String LOREMPIXEL_URL_TYPE_NIGHT = "nightlife";
    private static final String LOREMPIXEL_URL_TYPE_FASH = "fashion";
    private static final String LOREMPIXEL_URL_TYPE_PEOPLE = "people";
    private static final String LOREMPIXEL_URL_TYPE_NATURE = "nature";
    private static final String LOREMPIXEL_URL_TYPE_SPORTS = "sports";
    private static final String LOREMPIXEL_URL_TYPE_TECH = "technics";
    private static final String LOREMPIXEL_URL_TYPE_TRANSPORT = "transport";
    private static final String[] PLACEIMG_TYPES = {PLACEIMG_URL_TYPE_ANIMAL,PLACEIMG_URL_TYPE_ARCH,PLACEIMG_URL_TYPE_NATURE,PLACEIMG_URL_TYPE_PEOPLE,PLACEIMG_URL_TYPE_TECH};
    private static final String[] LOREMPIXEL_TYPES = {LOREMPIXEL_URL_TYPE_ABSTRACT, LOREMPIXEL_URL_TYPE_ANIMALS, LOREMPIXEL_URL_TYPE_BUSNESS, LOREMPIXEL_URL_TYPE_CATS, LOREMPIXEL_URL_TYPE_CITY, LOREMPIXEL_URL_TYPE_FOOD, LOREMPIXEL_URL_TYPE_NIGHT, LOREMPIXEL_URL_TYPE_FASH, LOREMPIXEL_URL_TYPE_PEOPLE, LOREMPIXEL_URL_TYPE_NATURE, LOREMPIXEL_URL_TYPE_SPORTS, LOREMPIXEL_URL_TYPE_TECH, LOREMPIXEL_URL_TYPE_TRANSPORT};
    private static final String PLACEIMG_URL = "http://placeimg.com/";
    private static final String LOREMPIXEL_URL = "http://lorempixel.com/";
    private static final String UNSPLASH_URL = "https://unsplash.it/";
    public static final int GENERATOR_TYPE_IMG_PLACEIMG = 1;
    public static final int GENERATOR_TYPE_IMG_LOREMPIXEL = 2;
    public static final int GENERATOR_TYPE_IMG_UNSPLASH = 3;

    /**
     *
     * @param width <=1200
     * @param height <=1200
     * @param source GENERATOR_TYPE_IMG_PLACEIMG,GENERATOR_TYPE_IMG_UNSPLASH,GENERATOR_TYPE_IMG_LOREMPIXEL
     * @return
     */
    public static String getImageUrl(int width, int height,int source) {
        Random rnd = new Random();
        if (width > 1200) width = 1200;
        if (height > 1200) height = 1200;
        switch (source) {
            case GENERATOR_TYPE_IMG_PLACEIMG:
                return PLACEIMG_URL + width + "/" + height + "/" + PLACEIMG_TYPES[rnd.nextInt(PLACEIMG_TYPES.length)];
            case GENERATOR_TYPE_IMG_UNSPLASH:
                return UNSPLASH_URL + width + "/" + height + "/?random";
            case GENERATOR_TYPE_IMG_LOREMPIXEL:
             return LOREMPIXEL_URL + width + "/" + height + "/" + LOREMPIXEL_TYPES[rnd.nextInt(LOREMPIXEL_TYPES.length)];
        }
        return "";
    }

    /**
     *
     * @param width <=1200
     * @param height <=1200
     * @return
     */
    public static String getImageUrl(int width, int height) {
        Random rnd = new Random();
        int type = rnd.nextInt(3);
        if (width > 1200) width = 1200;
        if (height > 1200) height = 1200;
        switch (type) {
            case GENERATOR_TYPE_IMG_PLACEIMG:
                return PLACEIMG_URL + width + "/" + height + "/" + PLACEIMG_TYPES[rnd.nextInt(PLACEIMG_TYPES.length)];
            case GENERATOR_TYPE_IMG_UNSPLASH:
                return UNSPLASH_URL + width + "/" + height + "/?random";
            case GENERATOR_TYPE_IMG_LOREMPIXEL:
                return LOREMPIXEL_URL + width + "/" + height + "/" + LOREMPIXEL_TYPES[rnd.nextInt(LOREMPIXEL_TYPES.length)];
        }
        return "";
    }

    public static String getString(int max) {
        return RandomStringUtils.randomAlphabetic(max);
    }

    public static String getString(int max,boolean withnums) {
        if (withnums) {
            return RandomStringUtils.randomAlphanumeric(max);
        }
        return RandomStringUtils.randomAlphabetic(max);
    }

    public static String getString(int max, String chars) {
        return RandomStringUtils.random(max, chars);
    }

    public static String getMaleName() {
        int length = WordsDB.getMales().length-1;
        int i = MathUtils.randInt(0, length);
        return WordsDB.getMales()[i];
    }

    public static String getFeMaleName() {
        int length = WordsDB.getFeMales().length-1;
        int i = MathUtils.randInt(0, length);
        return WordsDB.getFeMales()[i];
    }

    public static String getWord() {
        int length = WordsDB.getWords().length-1;
        int i = MathUtils.randInt(0, length);
        return WordsDB.getWords()[i];
    }

    public static String getStringAscii(int max) {
        return RandomStringUtils.randomAscii(max);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
