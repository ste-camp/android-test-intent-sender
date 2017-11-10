// ServiceInterface.aidl
package it.neslab.intentreceiver;

// Declare any non-default types here with import statements

interface ServiceInterface {

    long getCallTime();

    long getStart();

    long getIntentReceived();

    int getMode();

    int getNTests();

    long testMethod(int a, double b, inout Map c);
}
