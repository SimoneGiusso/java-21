# Introduction

Project to play with the new Java 21 Features

## Changes introduced after JDK 17 but before 21

### UTF-8 is now the default charset (18)

Standard Java APIs for reading and writing files and for processing text allow a*charset*to be passed as an argument. A
charset governs the conversion between raw bytes and the 16-bit`char`values of the Java programming language. If a
charset argument is not passed, then standard Java APIs typically use the*default charset*. The JDK chooses the default
charset at startup based upon the run-time environment: the operating system, the user's locale, and other factors.

However, now UTF-8 is the default for everyone.

### ****Internet-Address Resolution SPI (18)****

The [java.net.InetAddress](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/net/InetAddress.html) API
resolves host names to Internet Protocol (IP) addresses, and vice versa. The API currently uses the operating system's
native resolver.

A new service-provider interface (SPI) for host name and address resolution is made available to customize the resolver
used to do address resolution.

### Command-line tool to start a minimal web server that serves static files only (18)

Mainly for educational purposes. Nothing more.

### ****Reimplement Core Reflection with Method Handles****

Reimplement`java.lang.reflect.Method`,`Constructor`, and`Field`on top of`java.lang.invoke`method handles. It is just an
implementation change. Not performance change should be expected.

### New `@snippet` ****for Snippets in Java API Documentation****

To simplify the inclusion of example source code in API documentation, previously possible with:

```java
<pre>{@code
    lines of source code
        }</pre>
```

It overcomes some problems:

- There is no way for tools to reliably detect code fragments, in order to check their validity.
- etc…

It gives more options:

- Specify a reference to an external snippet
- etc…

### `Object#finalize()` method will be removed

The method is already deprecated:

```java
@Deprecated(since = "9") protected void finalize()throws Throwable{}
```

and now is deprecated from removal

```java
@Deprecated(since = "9", forRemoval = true)
protected void finalize()throws Throwable{}
```

In future release will be first disable by default then completely removed.

> 📝 Finalizers is an old mechanism to avoid resource leaks, but it has many disadvantages:
> - An arbitrarily long time may pass before its finalizer is called. In fact, is the GC that call this method just
    before gargage collect it. So the resource will kept open unnecessary, until is in fact GC.
> - Finalizer code can take any action. It could save a reference to the object being finalized, thereby*resurrecting*
    the object.
> - Every object created by a class with finalize implemented will be “finalized” even if it may be not necessary at
    some point (e.g. the source used was close before).
> - Neither threading nor ordering can be controlled, since they run on on unspecified threads.
    the *Try-with-resources* is the preferred mechanism*.*

## Changes introduced with JDK 21