import json
import os

def generate_crafting_recipe(jar_item, cooking_time, experience, ingredient, result_item, recipe_book_tab="meals"):
    recipe = {
        "type": "farmersdelight:cooking",
        "container": {
            "item": jar_item
        },
        "cookingtime": cooking_time,
        "experience": experience,
        "ingredients": [
            {"item": ingredient},
            {"item": ingredient},
            {"item": ingredient}
        ],
        "recipe_book_tab": recipe_book_tab,
        "result": {
            "item": result_item
        }
    }
    return recipe

def save_recipe_to_file(recipe, filename):
    with open(filename, 'w') as file:
        json.dump(recipe, file, indent=2)

def main():
    # Constants
    jar_item = "ukrainian_delight:jar"
    cooking_time = 200
    experience = 1.0

    # Prompt the user for the number of recipes
    num_recipes = int(input("Enter the number of recipes you want to create: "))
    recipes = []

    for i in range(num_recipes):
        print(f"\nRecipe {i + 1}:")
        # Input for the main ingredient
        ingredient = input("Enter the main ingredient (e.g., farmersdelight:tomato): ")
        
        # Generate file name and result item based on the main ingredient
        main_ingredient_name = ingredient.split(":")[1]
        result_item = f"ukrainian_delight:jarred_{main_ingredient_name}"
        
        recipe = generate_crafting_recipe(jar_item, cooking_time, experience, ingredient, result_item)
        recipes.append((recipe, main_ingredient_name))
    
    # Create the recipes directory if it doesn't exist
    os.makedirs('recipes', exist_ok=True)

    # Generate and save the recipes
    for recipe_data, main_ingredient_name in recipes:
        filename = f"recipes/jarring_{main_ingredient_name}.json"
        save_recipe_to_file(recipe_data, filename)
        print(f"Saved recipe to {filename}")

if __name__ == "__main__":
    main()
