# System Benchmark Report

## Objective

To measure the performance of the system's CPU, Memory, Disk, and
Graphics using the Windows System Assessment Tool (WinSAT). Benchmarking
helps evaluate whether the system is capable of handling development
environments such as Spring Boot, MongoDB, and IDEs efficiently.

------------------------------------------------------------------------

## Tool Used

**Windows System Assessment Tool (WinSAT)**\
A built-in Windows utility that evaluates hardware performance by
running standardized benchmark tests.

------------------------------------------------------------------------

## Commands Executed

  Command        Purpose
  -------------- -----------------------------------
  winsat query   Displays overall benchmark scores
  winsat cpu     Tests CPU performance
  winsat mem     Tests memory (RAM) throughput
  winsat disk    Tests disk read/write performance

------------------------------------------------------------------------

## Benchmark Results Summary

### CPU Performance

-   AES256 Encryption: **6065 MB/s**
-   SHA1 Hash: **1885 MB/s**
-   Compression: **933 MB/s**

**Inference:**\
The CPU shows high computational performance, suitable for compiling
applications and running backend services efficiently.

------------------------------------------------------------------------

### Memory (RAM) Performance

-   Memory Throughput: **28,366 MB/s**

**Inference:**\
High RAM speed ensures smooth multitasking, fast data access, and stable
performance for databases and development tools.

------------------------------------------------------------------------

### Disk Performance

-   Sequential Read: **2679 MB/s**
-   Sequential Write: **1348 MB/s**
-   Random Read: **660 MB/s**
-   Average Latency: **\< 0.2 ms**

**Inference:**\
The disk performance indicates NVMe SSD-level speed, providing fast
application loading, quick builds, and efficient database operations.

------------------------------------------------------------------------

### Graphics Performance

-   Direct3D Performance: **42 Frames/sec**

**Inference:**\
Graphics performance is moderate and sufficient for normal desktop and
development usage.

------------------------------------------------------------------------

## Overall System Evaluation

  Component   Performance Level
  ----------- -------------------
  CPU         High
  Memory      High
  Disk        Very High
  Graphics    Moderate

------------------------------------------------------------------------

## Conclusion

The benchmark results indicate that the system has strong processing
power, fast memory access, and high-speed storage. Therefore, it is
well-suited for running Java Spring Boot applications, MongoDB
databases, and software development environments without performance
bottlenecks.

------------------------------------------------------------------------

> The benchmark analysis confirms that the system hardware is capable of
> supporting modern backend development tasks efficiently.
