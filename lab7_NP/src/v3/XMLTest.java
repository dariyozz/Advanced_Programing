package v3;

import java.util.*;

interface XMLComponent {
    void addAttribute(String key, String value);
    String toString(int indentLevel);  // Override this to print XML representation with indentation
}

class XMLLeaf implements XMLComponent {
    private String tagName;
    private String value;
    private Map<String, String> attributes;

    public XMLLeaf(String tagName, String value) {
        this.tagName = tagName;
        this.value = value;
        this.attributes = new LinkedHashMap<>(); // Maintain attribute order
    }

    @Override
    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    @Override
    public String toString(int indentLevel) {
        String indent = "\t".repeat(indentLevel);
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("<").append(tagName);

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            sb.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }

        sb.append(">").append(value).append("</").append(tagName).append(">\n");
        return sb.toString();
    }
}

class XMLComposite implements XMLComponent {
    private String tagName;
    private Map<String, String> attributes;
    private List<XMLComponent> components;

    public XMLComposite(String tagName) {
        this.tagName = tagName;
        this.attributes = new LinkedHashMap<>(); // Maintain attribute order
        this.components = new ArrayList<>();
    }

    public void addComponent(XMLComponent component) {
        components.add(component);
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    @Override
    public String toString(int indentLevel) {
        String indent = "\t".repeat(indentLevel);
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("<").append(tagName);

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            sb.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }

        sb.append(">\n");

        for (XMLComponent component : components) {
            sb.append(component.toString(indentLevel + 1));
        }

        sb.append(indent).append("</").append(tagName).append(">\n");
        return sb.toString();
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        sc.nextLine();  // Consume the newline after the testCase

        // Test case 1: Simple XML element with attributes and value
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        // Test case 2: Composite structure with nested XML elements
        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");

        if (testCase == 1) {
            // Print the simple XML leaf component
            System.out.println(component.toString(0));
        } else if (testCase == 2) {
            // Print the composite XML structure
            System.out.println(composite.toString(0));
        } else if (testCase == 3) {
            // More complex composite structure with nested XML elements
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level", "1");

            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level", "2");

            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level", "3");
            lvl3.addComponent(component);

            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));

            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            // Print the complex nested XML structure
            System.out.println(main.toString(0));
        }
    }
}
