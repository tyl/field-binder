# FieldBinder Add-on

An advanced FieldGroup implementation with support for automated generation of Master/Detail forms.

![Master-Detail Demo](http://i.imgur.com/TZdfj5P.png)

## What is wrong with Good Ol' FieldGroup?

Vaadin's `FieldGroup` and `BeanFieldGroup` are non-visual components that build and automatically bind `Field`s to a data source. The data source may be an `Item` or a Java Bean. However, `FieldGroup`s have quite a few limitations:

* A FieldGroup only contains *bound* fields
* If a field is not bound to a datasource (using `unbind()`), then it is automatically *removed* from the FieldGroup. The FieldGroup is then unaware that the field exists. Trying to `getField()` returns `null`.
* A FieldGroup cannot `buildAndBind()` fields with unrecognized values, but it is extensible through a Factory; however, unrecognized values include `Collection` types, such as `List`s, which are typical of forms of the Master/Detail-type  
* There is no generic implementation of a standard Master/Detail editor (the JPAContainer `MasterDetailEditor` implementation is ad-hoc)
* There is no way to bind a FieldGroup to a Container: a FieldGroup is bound to an Item. Thus, there is no simple way to scan through a collection of records and display their contents in a form

## Meet the FieldBinder

The `FieldBinder` add-on is an advanced FieldGroup implementation. 

Features:

* A `FieldBinder<T>` binds to a Java Bean, like a BeanFieldGroup
* Managed Fields (built using `build()`) can be always `bind()`'ed and `unbind()`'ed; the FieldBinder will always keep track of them
* A FieldBinder can build() and bind() a component that displays a bean property even when it is a List<T>
* A FieldBinder can be bound to a Container. The `DataNavigation` interface provides commands to move an internal pointer to the next, previous, first and last Item in the Container (the Container must implement `Container.Ordered`: most `Container` implementations do, since it is required by `Table`)
* The `DataNavigation` interface provides commands to scan through a dataset and retrieving the `Item` that it points to.
* The `DataNavigation` interface provides standard behavior for performing CRUD operations (and, experimentally, lookup operations), which can be extended through a regular, Vaadin-style listener mechanism
* `FieldBinder` comes with standard built-in CRUD for the ListContainer and the [Lazy Mongo Container](https://github.com/tyl/mongodbcontainer-addon). The ListContainer should already cover most Java Bean use-cases. Support for Vaadin's official JPAContainer has landed in v1.1.  
* The `ButtonBar` component (and its relatives, `NavButtonBar`, `CrudButtonBar`, `FindButtonBar`) may be bound  to a `DataNavigation` and automatically generate buttons for user interactions (automatic shortcut key bindings will come soon!)
* ButtonBars are i18n compliant through the standard Java `ResourceBundle` mechanism 
* The `ListTable` and `BeanTable` wrappers augment Vaadin's regular Table with default behavior for basic CRUD.

![Detailed FieldBinder Taxonomy](http://i.imgur.com/RiFRfxJ.png)

## Table of Contents

* [Maven](#maven)
* [Demo](#demo-for-the-impatient)
* [Short Tutorial](#short-tutorial)
   * [What You Have Done](#what-you-have-done)
* [Extended Tutorial](#extended-tutorial)
   * [Final Touches](#final-touches)
* [DataNavigation](#datanavigation-1)
* [Bonus: Tabular Forms with `BeanTable<T>`](#bonus-tabular-forms-with-beantablet)

## Maven


Add `field-binder` to your Maven dependencies

```xml
<dependency>
   <groupId>org.tylproject.vaadin.addon.fieldbinder</groupId>
   <artifactId>field-binder</artifactId>
   <version>1.0</version>
</dependency>

<repository>
   <id>vaadin-addons</id>
   <url>http://maven.vaadin.com/vaadin-addons</url>
</repository>
```

If you want to use the SNAPSHOT version, clone this repository and install the package locally with

```sh
  $ git clone https://github.com/tyl/field-binder/
  $ mvn install 
```


## Demo (for the impatient)

Want to give an immediate look at the FieldBinder? Try the demo:

```sh
  $ cd demo
  $ mvn package && java -jar target/field-binder-demo-1.0.jar  
```

or simply:

```sh
  $ cd demo
  $ ./run.sh
```

The demo uses [vaadin4spring](https://github.com/peholmst/vaadin4spring) for convenience, and implements the two tutorials that follow.

## Short Tutorial
We will create a simple Address Book, where each `Person` may have many `Address`es. 
For conciseness, we will also use the [Viritin](https://github.com/viritin/viritin) addon, its `ListContainer` and the shorthand classes for Layouts. You do not need to add any further dependencies to your `pom`, though. The `field-binder` add-on depends on `Viritin` already. Now, let us write the following bean class for the Addresses:

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
   private Integer age;
   private Date birthDate;
   private List<Address> addressList = new ArrayList<Address>();
   public Person() {}
   public Person(String firstName, String lastName) {
     this.firstName = firstName;
     this.lastName = lastName;
   }
   
   /* ...getters and setters... */
}
```

Now you are ready to define your Vaadin `UI`

```java
@Title("Short Tutorial")
@Theme("valo")
public class ShortTutorial extends UI {
  
  // CONTAINER
  
  // initialize an empty container
  final FilterableListContainer<Person> container = 
                                    new FilterableListContainer<Person>(Person.class);
  
  // FIELD BINDER 
  // initialize the FieldBinder for the given container
  final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class, container);

  // initialize the layout, building the fields at the same time
  final VerticalLayout mainLayout = new MVerticalLayout(

      // auto-generates a button bar with the appropriate behavior
      // for the underlying FilterableListContainer
      new ButtonBar(binder.getNavigation().withDefaultBehavior()),

      new MFormLayout(
          // automatically generate a Field from the property type
          binder.build("firstName"),
          binder.build("lastName"),
          binder.build("birthDate"),
          binder.build("age"),
          
          // optional: display the index of the currentItem in the container
          new NavigationLabel(binder.getNavigation()) 

      ).withFullWidth().withMargin(true),

      // initialize the addressList field with a built-in button bar
      binder.buildListOf(Address.class, "addressList").withDefaultEditorBar()

  ).withFullWidth().withMargin(true);


  @Override
  protected void init(VaadinRequest request) {
    setContent(mainLayout);
  }

}
```

Start the Vaadin application: you have already a fully-functional master/detail editor in about 10 lines of actual code! 

Keep reading to know what happened under the hood, or skip to the next section for the extended tutorial.

### What You Have Done

Most of the code you have written is your regular Vaadin UI code. You only wrote about 10 lines of code. But has happened under the hood?  Let us have a look at the `FieldBinder`-specific code.

As you will see, you wrote very little, because a lot has been done already on your behalf!


#### Automatic Binding to a data source 

```java
  final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class, container);
```

With this code you are generating a FieldBinder for a `Person` bean. The *optional* container parameter tells the FieldBinder that you want to use the automatic navigation support. The `container` parameter is completely optional, and if you omit it, the `FieldBinder` will continue to work as an improved `FieldGroup`. In this case you must use the alternate constructor:

```java
  final FieldBinder<Person> binder = new FieldBinder<Person>(Person.class);
```

#### Automatic `Field` generation

Similarly to the `FieldGroup`, you can automatically `build()` fields for a given `Person` property. The `FieldBinder` will automatically infer the type of field. In our `Person` bean we have defined the properties `firstName`, `lastName`, `birthDate` (we will get back the `addressList` in a moment). Then we can write:

```java
          binder.build("firstName"),
          binder.build("lastName"),
          binder.build("birthDate"),
          binder.build("age"),       
```

More `build()` methods are available, along the lines of Vaadin's `FieldGroup`:

  * `build(Object propertyId)`
  * `build(String caption, Object propertyId)`
  * `build(String caption, Object propertyId, Class<T> fieldType)`

Refer to the JavaDoc and Vaadin's documentation to get more detailed information


Fields for `List` types must be generated using the method

```java
      binder.buildListOf(Address.class, "addressList")
```

the first argument is the type of the elements contained in the `List`. In our case, these are `Address`es; the second argument is the name of the property the contains the `List<Address>`, which, in our `Person` bean is called `addressList`. This method generates  `ListTable<Address>`, which is a thin wrapper around Vaadin's standard `Table`. The `ListTable<Address>` is a `Field` that differs slightly from Vaadin's `Table`. In  Vaadin's `Table`, `getValue()` returns the currently selected element, and `setValue(Object)` changes the currently selected item. In the `ListTable`, the  "value" is the *list of the values contained in the underlying Table*.

`ListTable<T>` uses Viritin's `ListContainer<T>` internally.


#### `DataNavigation` #####

Each `FieldBinder` that has been created with a `container` instance includes a `DataNavigation` instance that you can get using:

```java
      DataNavigation nav = binder.getNavigation()
```

The `DataNavigation` acts as a controller for scanning through a `Container`. For instance, you can use `nav.first()` to make the FieldBinder point to the first Item in the Container. The `DataNavigation` instance also fires events for each command that it defines. For instance, `nav.first()` issues a `FirstItem.Event` that you can listen to using `nav.addFirstItemListener(FirstItem.Listener listener)`. 

The `DataNavigation.withDefaultBehavior()` tries to guess the best predefined set of listeners for the container that you are currently using. In the case of this tutorial, you are using a `FilterableListContainer`. 

You are not required to delve into the details of this right now, but you can read more in the latter part of this document.

Each `ListTable` is also bound to a `DataNavigation` instance, which is kept in sync with the actual underlying `Table` selection (in other words, when you click an item in the `Table` that the `ListTable` wraps, the `currentItemId` of the `DataNavigation` instance is updated accordingly.



#### `ButtonBar` generation 

A `ButtonBar` instance must be attached to a DataNavigation instance, so it is enough to say:

```java
      DataNavigation nav = binder.getNavigation();
      ButtonBar buttonBar = new ButtonBar(nav);
```

or, on one line:

```java
      new ButtonBar(binder.getNavigation().withDefaultBehavior()),
```

The same can be done both for the navigation of a FieldBinder and the navigation of a ListTable. However, since the `Table` is also a Vaadin `Component` you can also make it generate an embedded button bar that is part of the `ListTable` itself. In this case you can write, as in the tutorial:

```java
      binder.buildListOf(Address.class, "addressList").withDefaultEditorBar()
```

Otherwise, you can create a button bar using code similar to the FieldBinder's:

 
```java
ListTable<Address> addressList = 
            binder.buildListOf(Address.class, "addressList");
ButtonBar addressListBar = new ButtonBar(addressList.getNavigation()
                                                    .withDefaultBehavior()),
```


The `ButtonBar` is a compound component that contains three distinct bars:

* `NavButtonBar`
* `CrudButtonBar`
* `FindButtonBar`

You can also choose to pick only one; for instance, if you just want the buttons for CRUD on the `addressList`:

```java
CrudButtonBar addressListBar = 
       new CrudButtonBar(addressList.getNavigation().withDefaultBehavior()),
```

#### NavigationLabel

This optional component shows the index of the current item and the total count of the items in the container that the `DataNavigation` is controlling.


## Extended Tutorial

In this version of the tutorial we will extend the regular editing behavior of the `DataNavigation` component with custom logic, using the built-in event listener system.

Let us start from the previous tutorial, in which we built a `Person` editor, with a detail view of the `Address` list. Each `Person` of the previous example contained an `age` field. As you have probably noticed, the `age` field is actually a function of the `birthDate` field: `age` is the difference in years between the current date and the `birthDate` of a `Person`. Let us implement a custom `BeforeCommit` listener that will update the `age` field with a computed value.
 
In order to simplify the writing of the custom events, it is advisable to assign the generated Fields to instance variables of the class; so, refactor them out as follows:

```java
  final TextField firstName = binder.build("firstName");
  final TextField lastName  = binder.build("lastName");
  final DateField birthDate = binder.build("birthDate");
  final TextField age       = binder.build("age");
  
  final ListTable<Address> addressList = binder.buildListOf(Address.class, "addressList")
                                               .withDefaultEditorBar();
```   

Then, the layout should be updated accordingly:

```java
  // initialize the layout
  final VerticalLayout mainLayout = new MVerticalLayout(

      new ButtonBar(binder.getNavigation().withDefaultBehavior()),

      new MFormLayout(
          firstName, lastName, birthDate, age,
          new NavigationLabel(binder.getNavigation())
      ).withFullWidth().withMargin(true),

      addressList

  ).withFullWidth().withMargin(true);
```


Now, let us write the custom actions as event listener. For convenience, we may define a separate class. 

```java

class MyController implements BeforeCommit.Listener {
  @Override
  public void beforeCommit(BeforeCommit.Event event) {
    // e.g., using JodaTime:
    DateTime birthDateValue = new DateTime(birthDate.getValue());
    int ageValue = Years.yearsBetween(birthDateValue, DateTime.now()).getYears();

    age.setConvertedValue(ageValue);
  } 
}

```

then, let us add the event listener to the `DataNavigation` of the `FieldBinder`

```java
  @Override
  protected void init(VaadinRequest request) {
    setContent(mainLayout);
    
    DataNavigation dataNav = binder.getNavigation();
    MyController controller = new MyController();
    dataNav.addBeforeCommitListener(controller);
  }
```

Start the application, and you're set!

### Final Touches

Since `age` is now a computed field you may want to set the *age* field to read-only to prevent users from modifying it. In this case, you may want to hook into the `ItemEdit` and `ItemCreate` events, and update the `BeforeCommit` listener accordingly:

```java
class MyController implements ItemEdit.Listener, ItemCreate.Listener, BeforeCommit.Listener {

  @Override
  public void itemEdit(ItemEdit.Event event) {
    age.setReadOnly(true);
  }

  @Override
  public void itemCreate(ItemCreate.Event event) {
    age.setReadOnly(true);
  }

  ...
  @Override
  public void beforeCommit(BeforeCommit.Event event) {
    // e.g., using JodaTime:
    DateTime birthDateValue = new DateTime(birthDate.getValue());
    int ageValue = Years.yearsBetween(birthDateValue, DateTime.now()).getYears();

    age.setReadOnly(false);
    age.setConvertedValue(ageValue);
    age.setReadOnly(true);
      
  }
}
```

don't forget to register the new event listeners:

```java
  @Override
  protected void init(VaadinRequest request) {
    setContent(mainLayout);
    DataNavigation dataNav = binder.getNavigation();

    MyController controller = new MyController();

    dataNav.addItemEditListener(controller);
    dataNav.addItemCreateListener(controller);
    dataNav.addBeforeCommitListener(controller);
  }
```

Now restart the application and see the result. In the next section we will give more details on the architecture of the FieldBinder and the related component. In particular, we will focus on the `DataNavigation` component. 





## DataNavigation


The `DataNavigation` interface implements navigation, CRUD (and experimentally) lookup methods for scanning through a Container. For instance, you can point to the first record

```java
      binder.getNavigation().first()
```

and then you can remove it

```java
      binder.getNavigation().remove()
```

The `DataNavigation` object wraps a `Container.Ordered` instance, and it maintains a pointer to the `currentItemId`. The `currentItemId` is `null` only when the `Container` is empty (`Container.size() == 0`) or when no container has been associated to the Navigator.


The `DataNavigation` interface defines several events that you can listen to.


### Navigation Events
	* FirstItem
	* NextItem
	* PrevItem
	* LastItem
	* CurrentItemChange
	

Because the DataNavigation object maintains an internal state, that is, a pointer to the "current" item id, for each other navigation event,  the `CurrentItemChange` event always fires. Therefore, if you want to hook into *every* navigation event, then you should listen to `CurrentItemChange`.

### CRUD events
	* ItemCreate
	* ItemEdit
	* ItemRemove
	* AfterCommit, OnCommit, BeforeCommit
	* OnDiscard
	
 	

For instance, in order to listen to the `CurrentItemChange` event on the `binder` component use:

```java
binder.getNavigation().addCurrentItemChangeListener(new CurrentItemChange.Listener(){
    public void currentItemChange(CurrentItemChange.Event event) {
       // display the updated current itemId in a notification
       Notification.show(event.getNewItemId());
    }
});
```

There is also a shorthand interface `CrudListeners` that implements all of the following listeners: `ItemCreate`, `ItemEdit`, `ItemRemove`, `OnCommit`, `OnDiscard`. If you need all of them you can just write `class MyController implements CrudListeners`. You can also tell a navigator to use all of the methods from a `CrudListeners` at once using:

```java
   navigation.withStrategy(new MyController())
```



### Lookup events (experimental)

   * ClearToFind
   * Find
   
The `DataNavigation` has experimental support for the *ClearToFind* and *Find* events. The ClearToFind event, "cleans" the fields of a FieldBinder for input, and makes it possible to perform a "search by example" (the same is obtained in a table using a pop-up window). These events can be attached using `ClearToFind.Listener` and `Find.Listener`, or both at once using `FindBehavior`.


![ClearToFind](http://i.imgur.com/AfrRFlT.png)

The default behavior for the ClearToFind event is to clear the fields when no filter has been applied; if a filter has been applied, the first click on the button will show the patterns that have been applied, and a second click will actually "clean" the search. Clicking the Find button will perform the search with the given criteria. For instance, in the example window of the picture, writing "Cor*" in the "First Name" field will find any Person whose name starts with the string "Cor"  

![Find](http://i.imgur.com/1ls24gW.png) 

When a filter has been applied (that is, `Container.getContainerFilters().isEmpty()` is false), the optional `NavigationLabel` displays an asterisk near the count.

The user can input a textual pattern, which will be translated into a Vaadin `Container.Filter` automatically. Supported filters are currently:

- for String-valued fields (`java.lang.String`) the following patterns generate a `SimpleStringFilter`:
  - `foo`: every string that starts with `foo`
  - `foo*`: same as above
  - `*foo*`: every string that contains `foo`
  - `*foo`: for a limitation in `SimpleStringFilter`, this is equivalent to `*foo*`.

- for integer-valued fields the following expression will generate Less, LessOrEqual, Greater, GreaterOrEqual, respectively: "<N", "<=N", ">N", ">=N". Where N is a number. For instance `>=10` on the field for property "age" will produce a `Compare.GreaterOrEqual("age", 10)`
 
 
For every other value, an exact match (`Equal`) is attempted.

The implementation of these conversions can be found in the `DefaultFilterFactory`, which implements a `FilterFactory`. This factory is used by a `FilterApplier`, which in turn applies filters over a container in a `DataNavigation`. The `FilterApplier` class is used by the default listeners that are created with `DataNavigation.withDefaultBehavior()`.


### How the "withDefaultBehavior()" method works


The `DataNavigation.withDefaultBehavior()` method tries to guess the best predefined set of listeners for the container that the `DataNavigation` is currently pointing to.
We will now outline how the resolving mechanism works. Besides `CrudBehavior` and `FindBehavior` there is a *third* short-hand interface that mixes in both of these interface with, in addition, `CurrentItemChange.Listener`; this is called the `Behavior` interface.

The `DataNavigation` contains a `BehaviorFactory` instance. This Factory tries to guess which collection of built-in listeners suits better the current configuration, depending whether the navigation controls a FieldBinder or a ListTable and depending on which container the navigation is currently wrapping.

When the `DataNavigation` controls a `FieldBinder`, then the factory is usually a `FieldBinderBehaviorFactory`; when it controls a `ListTable`, then this factory is usually a `TableBehaviorFactory`. In the case of this tutorial, your FieldBinder was controlled by a DataNavigation connected to a `FilterableListContainer`; thus the collection of listeners is taken from the `ListContainerBehavior` class. 

When the `DataNavigation.withDefaultBehavior()` method is invoked, the `BehaviorFactory` is given the current container (`DataNavigation.getContainer()`); if applicable, the factory returns an instance of the `Behavior` interface

## Bonus: Tabular Forms with `BeanTable<T>`

Because having forms with a lone Table is not uncommon, we also provide a stand-alone wrapper for a Table called a `BeanTable<T>`; it works similarly to the `ListTable<T>`, except it does not override the standard behavior of a table, with respect to selection. Methods `getValue()` and `setValue()` maintain their current semantics of "current selection". However, the `BeanTable` is a thin wrapper around Vaadin's Table which augment it with a `DataNavigation` instance, making it possible to obtain the same level of conciseness of the FieldBinder, but for a "Multi-occurrency"-type form:

```java

@Theme("valo")
public class TutorialTable extends UI {

  @Override
  protected void init(VaadinRequest request) {
    final Container.Ordered container = new ListTable<Person>(Person.class);

    final BeanTable<Person> table = new BeanTable<Person>(Person.class, container);
    table.setVisibleColumns("firstName", "lastName", "age", "birthDate");

    final ButtonBar bar = new ButtonBar(table.getNavigation()
                                             .withDefaultBehavior());

    final VerticalLayout mainLayout = new MVerticalLayout(bar, table)
                                      .withFullWidth()
                                      .withMargin(true);

    setContent(mainLayout);
  }
}

```


