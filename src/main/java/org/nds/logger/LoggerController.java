package org.nds.logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {

    @GetMapping("/log")
    public String log() {

        try {
            LoggerService.audit("Audit Message 1");
            LoggerService.audit("Audit Message 2", new Patient("John", "Doe"), new Patient("Jane", "Doe"));
            LoggerService.audit("Audit Message 3", false, new Patient("John", "Doe"), new Patient("Jane", "Doe"));

            throw new NullPointerException("Value is null");
        } catch(NullPointerException nullPointerException) {
            LoggerService.error("A null pointer exception", nullPointerException);
        }

        return "log";
    }

    static class Patient {

        private String firstName;
        private String lastName;

        public Patient(String firstName, String lastName) {
            this.firstName = firstName;
        }

        @Override
        public String toString() {
            return "Patient{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }
}
