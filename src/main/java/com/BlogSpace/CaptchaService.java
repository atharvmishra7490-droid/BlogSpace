package com.BlogSpace;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class CaptchaService {
    public static class CaptchaChallenge {
        public String question;
        public int answer;

        public CaptchaChallenge(String question, int answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    public CaptchaChallenge generateMathCaptcha() {
        Random rand = new Random();
        int num1 = rand.nextInt(15) + 1;
        int num2 = rand.nextInt(15) + 1;
        return new CaptchaChallenge("What is " + num1 + " + " + num2 + "?", num1 + num2);
    }
}