package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.helper.JsonHelper;
import com.company.artistmgmt.model.User;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class UserRepoImplTest {
    @Autowired
    private UserRepo repo;

    @Test
    public void getAllUsers() throws ArtistException {
        List<User> allUsers = repo.getAllUsers(1, 3);
        String serialize = JsonHelper.serialize(allUsers);
        System.out.println("All users:" + serialize);
    }

    @Test
    void GenerateCSVFile() {
        String filePath = "/home/prabin/Documents/cloco-project/artist-mgmt-service/artist_202412192150.csv";

        // Write to CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {"ID", "Name", "DOB", "Gender", "Address", "First Release Year", "No of Albums Released", "Created At", "Updated At"};

            // Data from CSV
            String[] data1 = {"10", "Charan Subedi", "2024-12-18 18:15:00", "1", "Kathmandu, Nepal", "2000", "10", "2024-12-19 18:51:25.154584", "2024-12-19 19:14:10.220699"};
            String[] data2 = {"11", "Sujan Chapagai", "2024-12-18 18:15:00", "2", "Kathmandu, Nepal", "2000", "10", "2024-12-19 19:36:30.418591", ""};
            String[] data3 = {"12", "Sujan Chapagai", "2024-12-18 18:15:00", "2", "Kathmandu, Nepal", "2000", "10", "2024-12-19 20:04:45.625984", ""};
            String[] data4 = {"13", "Sujan Chapagai", "2024-12-18 18:15:00", "2", "Kathmandu, Nepal", "2000", "10", "2024-12-19 20:07:20.478050", ""};
            writer.writeNext(header);
            writer.writeNext(data1);
            writer.writeNext(data2);
            writer.writeNext(data3);
            writer.writeNext(data4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Read from CSV
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println(String.join(", ", line));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}