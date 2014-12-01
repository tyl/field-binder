Master-Detail Addon
===================

A Vaadin logic component (non-visual) to automatically manage master/detail-type forms, by automatically wiring together regular Vaadin components.

A Master-Detail is bound to a `Container` through a `CrudNavigation` controller class. A `CrudNavigation`  class wraps a `Container` and maintains a pointer to a `currentItemId`. When the pointer moves to a different element of the `Container` the detail is updated accordingly.

The `CrudNavigation` component may be also used as stand-alone; it provides utility methods such as `next()`, `prev()`, `fist()`, `last()`. It also provides hooks (Vaadin event listeners) to implement CRUD functionalities. This addon comes with default implementations for the most used containers.

The result is smart scaffolding of a master-detail form, with little to none code required.

![Master-Detail Demo](https://bytebucket.org/evacchi/vaadin-masterdetail-addon/raw/16845e1d15321ac0e63c8b01498e177e4e91309c/readme-imgs/masterdetail-demo.png?token=951b7095d5439265892799b8e76a0607c88cebda)

## Demo (or, «The 0 Minutes Tutorial»)
Install the packages with Maven using

```sh
    $ mvn install
```

Then `cd` into the `masterdetail-demo` directory and launch with

```sh
   $ mvn jetty:run
```

Play with the code in `masterdetail-demo/src/main/java`

## The 10 Minutes Tutorial

Create a Vaadin project. If you use Maaven, use the Vaadin Archetype, and add this dependency to your `pom.xml`:

```xml
		<dependency>
			<groupId>org.tylproject.vaadin.addon.masterdetail</groupId>
			<artifactId>masterdetail-addon</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
```


In this quick tutorial you will realize the demo in a step-by-step fashion.
The tutorial consist of 6 simple steps:

1. Define the data model using Java Beans
2. Initialize a container for the master data source
3. Create the `FieldGroup` for the Master, and the `Table` for the detail
4. Create the `MasterDetail` instance, and bind it to the `FieldGroup` and the `Table`
5. Add everything to the UI
6. Create the (optional) button bars to control the Navigators for the Master and the Detail

### Define The Model

In this example, we will write a simple address book, where each `Person` may have many `Address`es. You would write the following bean class for the Addresses

```java
public class Address {
    private String street;
    private String zipCode;
    private String city;
    private String state;
    public Address() {}
    /* ...getters and setters... */
}
```

and the Person entity:

```java
public class Person {
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
```

If you plan to use  Mongo, you may also want to include an auto-generated `Id` field:
```java
   @Id private ObjectId id;
```

Once your model classes have been defined, then you can proceed to define the container from which you will pull instances of these classes.


### Create The Container
 
You may use a `BeanItemContainer<Person>`. As a better alternative, please consider [Maddon](https://github.com/mstahv/maddon)'s `ListContainer`, a drop-in replacement for `BeanItemContainer`, with better APIs and performances. This add-on supports it out of the box.

Define the ` masterDataSource` field in your UI class:

```java
  final BeanItemContainer<Person> masterDataSource = 
         new BeanItemContainer<Person>(Person.class, new ArrayList<Person>(Arrays.asList(
                new Person("George", "Harrison"),
                new Person("John", "Lennon"),
                new Person("Paul", "McCartney"),
                new Person("Ringo", "Starr")
        )));
```

It is also possible to take advantage of the built-in support for the [Lazy MongoContainer Addon](https://github.com/tyl/mongodbcontainer-addon). In this case, you would write something along the lines of:


```java
  final MongoContainer<Person> masterDataSource  = // makeDummyDataset();
        MongoContainer.Builder.forEntity(Person.class, makeMongoTemplate()).build();
  
  private static MongoOperations makeMongoTemplate() {
    try {
      return new MongoTemplate(new MongoClient("localhost"), "test");
    } catch (UnknownHostException ex) { throw new Error(ex); }
  }

```

### Create And Bind The MasterDetail

In your UI, define a `FieldGroup` to that will be bound to the `Person` objects, and the table that will be bound to the `List<Address>` of addresses.

```java
class MyVaadinUI extends UI {
  ...
  final FieldGroup fieldGroup = new FieldGroup();
  final Table table = new Table();
  ...
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

### Initialize The Layout


Generate your master form using the FieldGroup:

```java
  final TextField firstName = (TextField) fieldGroup.buildAndBind("firstName");
  final TextField lastName = (TextField) fieldGroup.buildAndBind("lastName");
``` 

Generate `ButtonBar`s to control the navigation and editing of the master/detail with default actions.

```java
  final ButtonBar masterBar = ButtonBar.forNavigation(masterDetail.getMaster().getNavigation());
  final ButtonBar detailBar = ButtonBar.forNavigation(masterDetail.getDetail().getNavigation());
```

Finally, initialize the form by adding the components to a layout as usual. 

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
        mainLayout.addComponent(new Panel(formLayout));

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

Now, run with 

```sh
  $ mvn jetty:run
```

## Hooking Into The Default Events

The `CrudNavigation` component defines several events that you can listen to 

* Navigation Events:
	* FirstItem
	* NextItem
	* PrevItem
	* LastItem
	* CurrentItemChange

Because the Navigation object maintains an internal state, that is, a pointer to the "current" item id, for each other navigation event,  the `CurrentItemChange` event always fires. Therefore, if you want to hook into *every* navigation event, then you should listen to `CurrentItemChange`.

* CRUD events
	* ItemCreate
	* ItemEdit
	* ItemRemove
	* AfterCommit, OnCommit, BeforeCommit
	* OnDiscard

For instance, in order to listen to the `CurrentItemChange` event on the `Master` component use:

```java
masterDetail.getMaster().getNavigation().addCurrentItemChangeListener(new CurrentItemChange.Listener(){
    public void currentItemChange(CurrentItemChange.Event event) {
       // display the updated current itemId in a notification
       Notification.show(event.getNewItemId());
    }
});
```

All the event listeners follow the same naming pattern, just substitute `CurrentItemChange` with the name of the event you want to listen to. If you want to hook into the navigation of a detail, just use `getDetail()` instead of `getMaster()`. For instance, listening to the `OnCommit` event of the detail, you would simply write:


```java
masterDetail.getDetail().getNavigation().addOnCommitListener(new OnCommit.Listener(){
    public void onCommit(OnCommit.Event event) {
       Notification.show("Commit Done").
    }
});
```


