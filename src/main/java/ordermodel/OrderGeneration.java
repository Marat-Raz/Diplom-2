package ordermodel;

import java.util.ArrayList;
import java.util.Random;

public class OrderGeneration {
    public final ArrayList<String> ingredients;

    public OrderGeneration(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }
    static String[] bread = new String[] { "61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa6d" }; // массив булок
    static String[] sauces = new String[] { "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa73",
            "61c0c5a71d1f82001bdaaa74", "61c0c5a71d1f82001bdaaa75" }; // массив соусов
    static String[] fillings =                                 // массив начинок
            new String[] { "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6e",
                    "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6e",
                    "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6e" };
    static int randomBread = new Random().nextInt(bread.length); // выбор случайной булки из массива
    static int randomSauces = new Random().nextInt(sauces.length); // выбор случайного соуса из массива
    static int randomFillings = new Random().nextInt(fillings.length); // выбор случайной начинки из массива

    public static OrderGeneration getIngredients(){
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(bread[randomBread]);
        ingredients.add(sauces[randomSauces]);
        ingredients.add(fillings[randomFillings]);
        return new OrderGeneration(ingredients);
    }
    public static OrderGeneration getEmptyIngredients(){
        ArrayList<String> ingredients = new ArrayList<>();
        return new OrderGeneration(ingredients);
    }
    public static OrderGeneration getIncorrectHashOfIngredients(){
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Wrong_Hash_61c0cwwyewr%63");
        ingredients.add("Wrong_Hash_Ytcvsf652424");
        return new OrderGeneration(ingredients);
    }


}
