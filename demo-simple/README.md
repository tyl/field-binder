# FieldBinder Add-on

Vaadin's `FieldGroup` is a non-visual component that builds and binds `Field`s to a data source, which may be an `Item` or, in the case of a `BeanFieldGroup<T>`, a Java Bean.
However, the standard `FieldGroup` implementation has quite a few limitations:

* It only keeps track of *bound* fields
* If a field is not bound to a datasource, then it is automatically *removed* from the group
* It is not possible to generate automatically a field for a bean property that is a collection type (e.g., a `java.util.List`)
* There is no generic implementation of a standard Master/Detail editor (the JPAContainer `MasterDetailEditor` implementation is ad-hoc)


The `FieldBinder` is an advanced FieldGroup implementationt. A `FieldBinder` can be used as a more flexible implementation of the `FieldGroup`, or it can be bound to a `Container` data source to enable automatic navigation of a data set, that can be hooked into through a  highly-customizable controller, called the `DataNavigation` component. The `DataNavigation` class wraps around a `Container.Ordered` implementation and enhances it with *state*. a `DataNavigation` maintains a `currentItemId`, which functions as a pointer to an `Item` of the underlying `Container` implementation. The `DataNavigation` provides commands to scan through a dataset and retrieving the `Item` that it points to. This is used by the `FieldBinder` to ...



Features:

* Builds and binds fields to an item or a bean data source
* Unbinds fields from a datasource without 