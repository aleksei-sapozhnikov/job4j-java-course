/**
 * Layer of "model" in Model-View-Controller pattern or "logic" layer. Takes objects,
 * validates them and if they are good, gives them to the "store" object which stores them
 * into needed storage.
 * <p>
 * General class is AbstractUserValidator which defines all actions. Extending classes use
 * one of the specific stores (collection, database, etc...)
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
package ru.job4j.crud.logic;