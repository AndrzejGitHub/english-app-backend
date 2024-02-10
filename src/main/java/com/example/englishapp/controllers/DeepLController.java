package com.example.englishapp.controllers;

import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.example.englishapp.models.dto.PartOfSpeechDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deepl/translate")
public class DeepLController {

    Translator translator;
    String authKey = "4eec8f29-a68e-ca55-cdd0-dd3b6de7c251:fx";

    @GetMapping("/en-pl")
    public ResponseEntity<TextResult> getTranslationEnglishGBToPolishFromDeepL(@RequestParam(name = "translation") String translation) throws Exception {
        translator = new Translator(authKey);
        TextResult result = translator.translateText(translation, "en", "pl");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/pl-en")
    public ResponseEntity<TextResult> getTranslationPolishToEnglishGBFromDeepL(@RequestParam(name = "translation") String translation) throws Exception {
        translator = new Translator(authKey);
        TextResult result = translator.translateText(translation, "pl", "en-GB");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/pl")
    public ResponseEntity<TextResult> getTranslationToPolishFromDeepL(@RequestParam(name = "translation") String translation) throws Exception {
        translator = new Translator(authKey);
        TextResult result = translator.translateText(translation, null, "pl");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/en")
    public ResponseEntity<TextResult> getTranslationToEnglishGBFromDeepL(@RequestParam(name = "translation") String translation) throws Exception {
        translator = new Translator(authKey);
        TextResult result = translator.translateText(translation, null, "en-GB");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/de")
    public ResponseEntity<TextResult> getTranslationToGermanFromDeepL(@RequestParam(name = "translation") String translation) throws Exception {
        translator = new Translator(authKey);
        TextResult result = translator.translateText(translation, null, "de");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}


