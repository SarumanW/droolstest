package lavka.drools.model.survey;

public enum Question {
    DIETS_QUESTION("Оберіть пункти, які найкраще вас описують:"),
    FORBIDDEN_PRODUCTS_QUESTION("Оберіть продукти, яких ви намагаєтесь уникати:");

    private String text;

    Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
