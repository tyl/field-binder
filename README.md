Master-Detail Addon
===================

## Demo
Install the packages with 

```sh
    $ mvn install
```

Then `cd` into the `masterdetail-demo` directory and launch with

```sh
   $ mvn jetty:run
```

## 10 Minutes Tutorial

Define your model through POJOs:

```java
public class Person {
   @Id private ObjectId id;
   private String firstName;
   private String lastName; 
   private List<Address> addressList = new ArrayList<Address>();
   public Person() {}
   public Person(String firstName, String lastName) {
     this.firstName = firstName;
     this.lastName = lastName;
   }
   /* ...getters and setters... */
}
public class Address {
    private String street;
    private String zipCode;
    private String city;
    private String state;
    public Address() {}
    /* ...getters and setters... */
}
```

In your UI, define a `FieldGroup` to that will be bound to the `Person` objects, and the table that will be bound to the `List<Address>` of addresses.

```java
class MyVaadinUI extends UI {
  ...
  final FieldGroup fieldGroup = new FieldGroup();
  final Table table = new Table();
  ...
}
```
Define the container. For instance, using a `BeanItemContainer<Person>` (as a better alternative, please consider [Maddon](https://github.com/mstahv/maddon)'s `ListContainer`, a drop-in replacement for `BeanItemContainer`, with better APIs and performances. This add-on supports it out of the box.)

```java
  final BeanItemContainer<Person> masterDataSource = 
         new BeanItemContainer<Person>(Person.class, new ArrayList<Person>(Arrays.asList(
                new Person("George", "Harrison"),
                new Person("John", "Lennon"),
                new Person("Paul", "McCartney"),
                new Person("Ringo", "Starr")
        )));
```

Or, using built-in support for the [Lazy MongoContainer Addon](https://github.com/tyl/mongodbcontainer-addon). For instance:


```java
  final MongoContainer<Person> masterDataSource  = // makeDummyDataset();
        MongoContainer.Builder.forEntity(Person.class, makeMongoTemplate()).build();
  
  private static MongoOperations makeMongoTemplate() {
    try {
      return new MongoTemplate(new MongoClient ("localhost"), "test");
    } catch (UnknownHostException ex) { throw new Error(ex); }
  }

```

Bind the Table as the FieldGroup's detail using the syntax:

```java
  // generates the MasterDetail class
  final MasterDetail masterDetail = MasterDetail.with(

   Master.of(Person.class)
     .fromContainer(masterDataSource)
     .boundTo(fieldGroup)
     .withDefaultCrud(),
     
   Detail.collectionOf(Address.class)
     .fromMasterProperty("addressList")
     .boundTo(table)
     .withDefaultCrud()

  ).build();
```

Then, you can generate your master form using the FieldGroup:

```java
  final TextField firstName = (TextField) fieldGroup.buildAndBind("firstName");
  final TextField lastName = (TextField) fieldGroup.buildAndBind("lastName");
``` 

You can generate `ButtonBar`s to control the navigation and editing of the master/detail with default actions.

```java
  final ButtonBar masterBar = ButtonBar.forNavigation(masterDetail.getMaster().getNavigation());
  final ButtonBar detailBar = ButtonBar.forNavigation(masterDetail.getDetail().getNavigation());
```

You should then initialize the form by adding the components to a layout as usual. 

```java
    final VerticalLayout mainLayout = new VerticalLayout();
    final FormLayout formLayout = new FormLayout();
    
    @Override
    protected void init(VaadinRequest request) {
        setupMainLayout();
        setupFormLayout();
        setupTable();
        setContent(mainLayout);
    }

    private void setupMainLayout() {
        mainLayout.setMargin(true);

        mainLayout.addComponent(masterBar.getLayout());
        mainLayout.addComponent(formLayout);

        mainLayout.addComponent(detailBar.getLayout());
        mainLayout.addComponent(table);
    }

    private void setupFormLayout() {
        formLayout.setMargin(true);
        formLayout.setHeightUndefined();

        formLayout.addComponent(firstName);
        formLayout.addComponent(lastName);
    }

    private void setupTable() {
        table.setSelectable(true);
        table.setSizeFull();
    }
```

For a full example, see the demo.
