package ru.homethings.overall.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "overall_promises")
public class Promise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(name = "begin_date", updatable = false)
    private LocalDate beginDate;

    @Column(name = "end_date", updatable = false)
    private LocalDate endDate;
}
