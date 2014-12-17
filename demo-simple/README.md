# FieldBinder Add-on

Vaadin's `FieldGroup` is a non-visual component that builds and binds `Field`s to a data source, which may be an `Item` or, in the case of a `BeanFieldGroup<T>`, a Java Bean.
However, the standard `FieldGroup` implementation has quite a few limitations:

* It only keeps track of *bound* fields
* If a field is not bound to a datasource, then it is automatically *removed* from the group
* It is not possible to generate automatically a field for a bean property that is a collection type (e.g., a `java.util.List`)
* There is no generic implementation of a standard Master/Detail editor (the JPAContainer `MasterDetailEditor` implementation is ad-hoc)

...

The FieldBinder is an advanced FieldGroup implementation.
 


Features:

* Builds and binds fields to an item or a bean data source
* Unbinds fields from a datasource without 