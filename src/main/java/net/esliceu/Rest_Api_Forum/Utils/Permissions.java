package net.esliceu.Rest_Api_Forum.Utils;

import net.esliceu.Rest_Api_Forum.Entities.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Permissions {
    private List<String> root = new ArrayList<>();
    private HashMap<String, List<String>> categories = new HashMap<>();
    public Permissions(Boolean admin, List<Category> categories) {
        root.add("own_topics:write");
        root.add("own_topics:delete");
        root.add("own_replies:write");
        root.add("own_replies:delete");
        if(admin){
            root.add("categories:write");
            root.add("categories:delete");
        }
        for(Category category : categories){
            List<String> result = new ArrayList<>();
            result.add("categories_topics:write");
            result.add("categories_topics:delete");
            result.add("categories_replies:write");
            result.add("categories_replies:delete");
            this.categories.put(category.getTitle(), result);
        }
    }

    public List<String> getRoot() {
        return root;
    }

    public void setRoot(List<String> root) {
        this.root = root;
    }

    public HashMap<String, List<String>> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, List<String>> categories) {
        this.categories = categories;
    }
}
