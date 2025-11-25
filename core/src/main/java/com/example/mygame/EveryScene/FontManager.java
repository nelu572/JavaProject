package com.example.mygame.EveryScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public class FontManager {

    private static final HashMap<String, BitmapFont> fonts = new HashMap<>();

    private FontManager() {
        // 외부에서 인스턴스 생성 못하도록 private
    }

    /**
     * TTF 파일로 BitmapFont 생성 후 관리
     * @param fontName 구분용 이름
     * @param filePath assets 폴더 내 TTF 경로
     * @param size 글자 크기
     * @param color 글자 색상
     * @param additionalChars 한글 등 추가 문자
     */
    public static void loadFont(String fontName, String filePath, int size, Color color, String additionalChars) {
        if (fonts.containsKey(fontName)) return; // 이미 로드된 폰트면 무시

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filePath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + additionalChars;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        fonts.put(fontName, font);
    }

    /**
     * 로드된 폰트 반환
     */
    public static BitmapFont getFont(String fontName) {
        return fonts.get(fontName);
    }

    /**
     * 모든 폰트 메모리 해제
     */
    public static void dispose() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();
    }
}
