package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class WordsUtils {

    @Cacheable("download_stop_words")
    public List<String> downloadStopWords() {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("keyword/russian_stop_words.txt");
             InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(resource), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new ServiceException("Ошбика чтения из файла", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Получение очищенно массива слов
     */
    public List<String> getCleanedWords(String text) {
        //очистка текста от спец. символов
        var cleanedText = text.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "");

        //подгрузка стоп слов из файла
        var stopWords = downloadStopWords();

        //очистка текста от стоп слов
        var cleanedWords = new HashSet<String>();
        var allWords = cleanedText.toLowerCase().split(" ");
        for (var word : allWords) {
            if (!stopWords.contains(word) && StringUtils.isNotBlank(word)) {
                cleanedWords.add(word);
            }
        }

        //возвращаем "очищенный" массив слов
        return new ArrayList<>(cleanedWords);
    }

    /**
     * Вычисление сходство (коэффицент от 0 до 1) между двумя строками.
     * Чем больше коэффицент, тем более схожи строки
     */
    public double similarity(String s1, String s2) {
        //если хотя бы одна строка пустая, то вовращаем нулевой коэффицент
        if (StringUtils.isEmpty(s1) || StringUtils.isEmpty(s2)) {
            return 0;
        }

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        var longer = s1;
        var shorter = s2;

        //переменная longer всегда должно иметь большую длину
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        //использование метрики "Расстояние Левенштейна"
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return (longer.length() - levenshteinDistance.apply(longer, shorter)) / (double) longer.length();
    }
}
