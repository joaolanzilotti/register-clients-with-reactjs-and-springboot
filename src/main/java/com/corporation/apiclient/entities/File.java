package com.corporation.apiclient.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Files")
public class File implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contentType;

    private Long size;

    @Lob
    private byte[] data;

    public File(String name, String contentType, Long size, byte[] data) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.data = data;
    }
}
