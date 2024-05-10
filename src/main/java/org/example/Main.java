package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        File filePath = new File("C:\\Users\\Sergey\\Downloads\\Задача ВС Java Сбер.csv");

        List<City> cities = readCityData(filePath);

        Map<String, Integer> regionCityCount = countCitiesInRegions(cities);

        for (Map.Entry<String, Integer> entry : regionCityCount.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    private static List<City> readCityData(File filePath) {
        List<City> cities = new ArrayList<>();

        try {
            File file = new File(String.valueOf(filePath));
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] cityData = line.split(";");

                if (cityData.length == 6) {
                    int id = Integer.parseInt(cityData[0]);
                    String name = cityData[1];
                    String region = cityData[2];
                    String district = cityData[3];
                    int population = Integer.parseInt(cityData[4]);
                    int foundationYear = parseFoundationYear(cityData[5]);
                    City city = new City(id, name, region, district, population, foundationYear);
                    cities.add(city);
                    id++;
                } else if (cityData.length == 5) {
                    int id = Integer.parseInt(cityData[0]);
                    String name = cityData[1];
                    String region = cityData[2];
                    String district = cityData[3];
                    int population = Integer.parseInt(cityData[4]);
                    int foundationYear = 0;
                    City city = new City(id, name, region, district, population, foundationYear);
                    cities.add(city);
                    id++;
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return cities;
    }
    private static Map<String, Integer> countCitiesInRegions(List<City> cities) {
        Map<String, Integer> regionCityCount = new HashMap<>();

        for (City city : cities) {
            String region = city.getRegion();
            if (regionCityCount.containsKey(region)) {
                int count = regionCityCount.get(region);
                regionCityCount.put(region, count + 1);
            } else {
                regionCityCount.put(region, 1);
            }
        }

        return regionCityCount;
    }
    private static int parseFoundationYear(String yearString) {
        int foundationYear = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

            Date date = dateFormat.parse(yearString);
            foundationYear = Integer.parseInt(dateFormat.format(date));

            if (foundationYear < 1000) {
                foundationYear *= 100;
            }
        } catch (ParseException | NumberFormatException e1) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                dateFormat.setLenient(false);

                int index = yearString.lastIndexOf(' ');
                if (index != -1) {
                    yearString = yearString.substring(0, index);
                }
                Date date = dateFormat.parse(yearString);
                foundationYear = Integer.parseInt(dateFormat.format(date));
            } catch (ParseException | NumberFormatException e2) {
                System.out.println("Ошибка при обработке года основания: " + yearString);
            }
        }
        return foundationYear;
    }
}