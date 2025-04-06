package service;

import domain.Animal;
import domain.enums.AnimalType;
import domain.enums.BiologicalSex;
import domain.filterStrategy.AnimalFilterStrategy;
import repository.AnimalRepositoryImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AnimalService {


    private final AnimalRepositoryImpl animalRepository;
    private final FileWriterService fileWriterService;

    public AnimalService(FileWriterService fileWriterService) {
        this.animalRepository = AnimalRepositoryImpl.getInstance();
        this.fileWriterService = fileWriterService;
    }

    //TODO test saveAnimal()
    public void saveAnimal(String firstName, String lastName, AnimalType animalType, BiologicalSex biologicalSex, Integer addressNumber, String addressName, String addressCity, Double age, Double weight, String breed) throws IOException {
        //Validate name and surname content
        if (containsInvalidCharacters(firstName) || containsInvalidCharacters(lastName)) {
                throw new IllegalArgumentException("First and last names must contain only A-Z letters.");
        }

        //validate breed content
        if (!breed.trim().isEmpty()) {
            if (containsInvalidCharacters(breed)) {
                throw new IllegalArgumentException("Breed must contain only A-Z letters.");
            }
        }


        //validate minimum and maximum age
        if (age != null && (age <= 0 || age > 20)) {
            throw new IllegalArgumentException("Age must be between 0.1 and 20.");
        }

        //validate min and max weight
        if (weight != null && (weight > 60 || weight < 0.1)) {
            throw new IllegalArgumentException("Weight must be between 0.1 and 60.");
        }

        //create new animal
        Animal animal = new Animal(firstName,
                lastName,
                animalType,
                biologicalSex,
                addressNumber,
                addressName,
                addressCity,
                age,
                weight,
                breed);
        System.out.println("DEBUG SERVICE: Trying to save -> " + animal);
        animalRepository.save(animal);
        System.out.println("DEBUG SERVICE: Save method executed");
        System.out.println("Animal created in Service" + animal);
        fileWriterService.createAnimalFile(animal);

    }

    public List<Animal> listAll() {
       List<Animal> animals = animalRepository.findAll();

       if (animals.isEmpty()) {
           System.out.println("No registered animals.");
       }

       return animals;
    }

    public List<Animal> filterAnimals(AnimalType animalType, Map<AnimalFilterStrategy, Object> filters) {
        List<Animal> filteredAnimals = animalRepository.findAll();


        return filteredAnimals;
    }

    private boolean containsInvalidCharacters(String text) {
        return text == null || !text.matches("[a-zA-Z ]+");
    }

    public boolean isValidDecimal(String input) {
        return input.matches("^[+-]?\\d+(?:[.,]\\d+)?$");
    }

}
