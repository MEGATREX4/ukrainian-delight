import os
import json

def create_json_files(directory):
    # Check if the directory exists
    if not os.path.exists(directory):
        print(f"Directory not found: {directory}")
        return
    
    # Print the directory being processed
    print(f"Processing directory: {directory}")

    # Iterate through all files in the directory
    for filename in os.listdir(directory):
        # Print the name of each file found
        print(f"Found file: {filename}")
        
        if filename.endswith(".png"):
            # Remove the file extension to get the item name
            item_name = os.path.splitext(filename)[0]
            print(f"Processing PNG file: {filename} -> {item_name}")

            # Create the JSON content
            json_content = {
                "parent": "item/generated",
                "textures": {
                    "layer0": f"ukrainian_delight:item/krashanky/{item_name}"
                }
            }

            # Define the output JSON file path
            json_filename = f"{item_name}.json"
            json_filepath = os.path.join(directory, json_filename)
            
            # Write the JSON content to the file
            with open(json_filepath, 'w') as json_file:
                json.dump(json_content, json_file, indent=2)
                
            print(f"Created JSON file: {json_filepath}")
        else:
            print(f"Skipped non-PNG file: {filename}")

# Replace 'your_directory_path' with the path where your PNG files are located
create_json_files('D:\\Users\\EUGENE\\Desktop\\mods\\ukrainian_delight\\src\\main\\resources\\assets\\ukrainian_delight\\textures\\item\\krashanky')
