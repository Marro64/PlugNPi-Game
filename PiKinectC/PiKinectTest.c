#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <libfreenect.h>

#ifdef _MSC_VER
#define HAVE_STRUCT_TIMESPEC
#endif
#include <pthread.h>

#if defined(__APPLE__)
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#define _USE_MATH_DEFINES
#include <math.h>

freenect_context *f_ctx;

int main(int argc, char **argv)
{
    printf("Hello world!\n");
    
    if (freenect_init(&f_ctx, NULL) < 0) {
		printf("freenect_init() failed\n");
		return 1;
	}
}