package io.ylab.intensive.lesson05.messagefilter.service;

import io.ylab.intensive.lesson05.messagefilter.dao.SwearwordDao;
import io.ylab.intensive.lesson05.messagefilter.rabbit.RabbitProducer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    private final SwearwordDao dao;
    private final RabbitProducer producer;

    public MessageServiceImpl(SwearwordDao dao, RabbitProducer producer) {
        this.dao = dao;
        this.producer = producer;
    }

    public void processMessage(String msg) {

        String result = replaceSwearwords(msg);

        producer.sendMessage(result);
    }

    private String replaceSwearwords(String sentence) {
        // Разбиваем по границам слов на основе ТЗ
        String wordBoundary = "(?<=([.,?!;\\s]))(?=\\b)|(?<=\\b)(?=([.,?!;\\s]))";
        String wordRegex = "[.,?!;\\s]*";

        String[] splitted = sentence.split(wordBoundary);
        List<String> result = new ArrayList<>();
        for (String s: splitted) {
            // Проверяем только слова
            if (!s.matches(wordRegex) && dao.existsIgnoreCase(s)) {
                s = replaceSequenceWithStars(s);
            }
            result.add(s);
        }
        return String.join("", result);
    }

    private String replaceSequenceWithStars(String w) {
        CharSequence sequenceToReplace = w.subSequence(1, w.length() - 1);
        char[] arr = new char[sequenceToReplace.length()];
        Arrays.fill(arr, '*');
        String replacement = new String(arr);
        return w.replace(sequenceToReplace, replacement);
    }
}
