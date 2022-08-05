import matplotlib.pyplot as plt
import numpy as np
def f(w, p):
    return 10*w/((1-p)*(1-p**3))

p = np.linspace(0, 0.8 , 300)
w = np.linspace(0,50 , 300)

P, W = np.meshgrid(p, w)
Z = f(W, P)
ax = plt.subplot(projection='3d')
ax.contour3D(P, W, Z, 5000, cmap='viridis')
plt.xlabel("Probability")
plt.ylabel("Width")
plt.show()