package ru.homethings.overall.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.homethings.overall.model.Promise;
import ru.homethings.overall.repositories.PromiseRepository;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private PromiseRepository promiseRepository;

    public DataLoader(PromiseRepository promiseRepository) {
        this.promiseRepository = promiseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Promise promiseOne = new Promise();
        promiseOne.setText("To learn Java");
        promiseOne.setBeginDate(LocalDate.of(2022, 1, 1));
        promiseOne.setEndDate(LocalDate.of(2023, 1, 1));

        Promise promiseTwo = new Promise();
        promiseTwo.setText("Read 5 books");
        promiseTwo.setEndDate(LocalDate.of(2024, 5, 6));

        promiseRepository.save(promiseOne);
        promiseRepository.save(promiseTwo);
    }
}
