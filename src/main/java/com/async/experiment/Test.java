package com.async.experiment;

import com.async.FunctionInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    static class Animal {
        private String name;
        private int age;

        public Animal(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    static class Dog extends Animal {
        private String breed;

        public Dog(String name, int age, String breed) {
            super(name, age);
            this.breed = breed;
        }

        public String getBreed() {
            return breed;
        }
    }

    static class SubDog extends Dog {
        private String chip;

        public SubDog(String name, int age, String breed, String chip) {
            super(name, age, breed);
            this.chip = chip;
        }
    }

    public static Animal getAnimal() {
        Dog dog = new Dog("dog", 1, "breed");
        return dog;
    }

    public static void main(String[] args) {
        Animal animal = new Animal("animal", 1);
        Dog dog = new Dog("dog", 1, "breed");
        SubDog subDog = new SubDog("subDog", 1, "breed", "chip");
        List<Dog> a = new ArrayList();


//        a.add(subDog);
//        a.add(dog);
//        SubDog object2 = (SubDog) a.get(0);
        FunctionInterface.ThrowingFunction<? super Dog, ? extends Animal> dataTransform =
                new FunctionInterface.ThrowingFunction< Animal, Dog>() {


                    @Override
                    public Dog apply(Animal t) throws Throwable {
                        return null;
                    }
                };


        ArrayList<? super Animal> list = new ArrayList<>();
        list.add(null);
        list.add(new Animal("animal", 1));
        list.add(new SubDog("subDog", 1, "breed", "chip"));
        list.get(1);


        List<Animal> list4 = Arrays.asList(dog, dog, dog, animal);
        List<Dog> list3 = Arrays.asList(dog, dog, dog);
//        List<? super Animal> test = list3;
        List<Animal> list1 = Arrays.asList(animal, dog);


//        list1.add(dog); //runtime exception
        list.add(animal);//will get error
        list.add(dog);//will get error
        Object object1 = list.get(0);
        Object object = list.get(0);
        Animal animal2 = list1.get(0);
        Animal animal3 = list1.get(1);

        list1.add(new Dog("dog", 1, "breed"));

        List<? super Animal> list2 = Arrays.asList(animal, dog);
        list2.add(new Dog("dog", 1, "breed"));

//        System.out.println(animal1.getName());

        List<String> name = new ArrayList<String>();
        List<Integer> age = new ArrayList<Integer>();
        List<Number> number = new ArrayList<Number>();


    }
}
