# Lamport Timestamp Messaging System

This repository contains an implementation of a Lamport timestamp-based messaging system in Java. The system allows for direct message exchange between threads, addressing potential bottlenecks and enhancing overall system throughput without relying on centralized architectures, ensuring consistent order across multiple threads without relying on physical clocks.

## Description

In the previous Repo, the message sequencer posed a potential bottleneck, impacting system throughput. To overcome this limitation, Lamport Timestamps are employed to achieve total ordering of messages without dependency on physical clocks or centralized architectures.

The redesigned application enables threads to directly exchange messages. When a thread reads an external message from its inbox, it distributes the content as an internal message to the inbox of every other thread. Threads agree on a total order of messages by utilizing Lamport Timestamps, ensuring consistency across the system.

Each thread maintains a sequence of messages reflecting the order according to the Lamport Timestamps. Upon termination of the program, all threads write their sequence to a thread-specific log file. Successful execution results in all threads reporting the same sequence of messages.

## Technical Requirements

- Threads directly exchange messages without reliance on a centralized message sequencer.
- Lamport Timestamps are utilized to establish a total order of messages.
- Each thread maintains a sequence of messages based on Lamport Timestamps.
- Upon program termination, all threads write their message sequence to a thread-specific log file.
